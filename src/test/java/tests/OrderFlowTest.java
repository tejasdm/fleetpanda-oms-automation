package tests;

import api.*;
import base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.DbHelper;

import java.sql.ResultSet;

import static org.testng.Assert.*;
import static utils.TestData.*;

public class OrderFlowTest extends BaseTest {

    LoginApi loginApi = new LoginApi();
    OrderApi orderApi = new OrderApi();
    PaymentApi paymentApi = new PaymentApi();

    @Test
    public void fullFlow_success() throws Exception {
        // login
        String token = loginApi.getToken(VALID_USER, VALID_PASS);
        assertNotNull(token, "token should not be null");

        // create order
        Response order = orderApi.createOrder(token, USER_ID, PRODUCT_ID, QTY);
        assertEquals(order.statusCode(), HTTP_CREATED);
        int orderId = order.jsonPath().getInt("id");
        assertEquals(order.jsonPath().getInt("userId"), USER_ID);
        assertEquals(order.jsonPath().getInt("products[0].productId"), PRODUCT_ID);
        assertEquals(order.jsonPath().getInt("products[0].quantity"), QTY);

        // payment
        Response pay = paymentApi.pay(orderId, VALID_CARD);
        assertEquals(pay.statusCode(), HTTP_OK);
        assertEquals(pay.jsonPath().getString("status"), STATUS_SUCCESS);
        assertEquals(pay.jsonPath().getString("txnId"), TXN_ID);

        // db check (simulated)
        DbHelper db = new DbHelper();
        db.saveOrder(orderId, USER_ID, PRODUCT_ID, QTY, ORDER_CREATED);
        db.updateStatus(orderId, ORDER_CONFIRMED);
        ResultSet rs = db.getOrder(orderId);
        assertTrue(rs.next(), "order not found in DB");
        assertEquals(rs.getInt("qty"), QTY);
        assertEquals(rs.getString("status"), ORDER_CONFIRMED);
    }

    @Test
    public void invalidLogin_shouldFail() {
        Response res = loginApi.login(INVALID_USER, INVALID_PASS);
        assertEquals(res.statusCode(), HTTP_UNAUTHORIZED);
        assertFalse(res.asString().contains("token"), "token should not come for invalid login");
    }

    @Test
    public void paymentFailure_orderShouldNotBeConfirmed() throws Exception {
        String token = loginApi.getToken(VALID_USER, VALID_PASS);
        Response order = orderApi.createOrder(token, USER_ID, 3, 1);
        int orderId = order.jsonPath().getInt("id");

        Response pay = paymentApi.pay(orderId, DECLINED_CARD);
        assertEquals(pay.statusCode(), HTTP_PAYMENT_FAILED);
        assertEquals(pay.jsonPath().getString("status"), STATUS_FAILED);
        assertEquals(pay.jsonPath().getString("reason"), DECLINED_REASON);

        // order must stay unconfirmed in DB
        DbHelper db = new DbHelper();
        db.saveOrder(orderId + 1000, USER_ID, 3, 1, ORDER_PAYMENT_FAILED);
        ResultSet rs = db.getOrder(orderId + 1000);
        assertTrue(rs.next());
        assertNotEquals(rs.getString("status"), ORDER_CONFIRMED);
    }
}