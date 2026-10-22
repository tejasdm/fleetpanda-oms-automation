package tests;

import api.*;
import base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.DbHelper;

import java.sql.ResultSet;

import static org.testng.Assert.*;

public class OrderFlowTest extends BaseTest {

    LoginApi loginApi = new LoginApi();
    OrderApi orderApi = new OrderApi();
    PaymentApi paymentApi = new PaymentApi();

    static final String USER = "mor_2314";
    static final String PASS = "83r5^_";

    @Test
    public void fullFlow_success() throws Exception {
        // login
        String token = loginApi.getToken(USER, PASS);
        assertNotNull(token, "token should not be null");

        // create order
        Response order = orderApi.createOrder(token, 1, 5, 2);
        assertEquals(order.statusCode(), 201);
        int orderId = order.jsonPath().getInt("id");
        assertEquals(order.jsonPath().getInt("userId"), 1);
        assertEquals(order.jsonPath().getInt("products[0].productId"), 5);
        assertEquals(order.jsonPath().getInt("products[0].quantity"), 2);

        // payment
        Response pay = paymentApi.pay(orderId, "4111111111111111");
        assertEquals(pay.statusCode(), 200);
        assertEquals(pay.jsonPath().getString("status"), "SUCCESS");
        assertNotNull(pay.jsonPath().getString("txnId"));

        // db check (simulated)
        DbHelper db = new DbHelper();
        db.saveOrder(orderId, 1, 5, 2, "CREATED");
        db.updateStatus(orderId, "CONFIRMED");
        ResultSet rs = db.getOrder(orderId);
        assertTrue(rs.next(), "order not found in DB");
        assertEquals(rs.getInt("qty"), 2);
        assertEquals(rs.getString("status"), "CONFIRMED");
    }

    @Test
    public void invalidLogin_shouldFail() {
        Response res = loginApi.login("wronguser", "wrongpass");
        assertEquals(res.statusCode(), 401);
        assertFalse(res.asString().contains("token"), "token should not come for invalid login");
    }

    @Test
    public void paymentFailure_orderShouldNotBeConfirmed() throws Exception {
        String token = loginApi.getToken(USER, PASS);
        Response order = orderApi.createOrder(token, 1, 3, 1);
        int orderId = order.jsonPath().getInt("id");

        Response pay = paymentApi.pay(orderId, "4000000000000002");
        assertEquals(pay.statusCode(), 402);
        assertEquals(pay.jsonPath().getString("status"), "FAILED");

        // order must stay unconfirmed in DB
        DbHelper db = new DbHelper();
        db.saveOrder(orderId + 1000, 1, 3, 1, "PAYMENT_FAILED");
        ResultSet rs = db.getOrder(orderId + 1000);
        assertTrue(rs.next());
        assertNotEquals(rs.getString("status"), "CONFIRMED");
    }
}