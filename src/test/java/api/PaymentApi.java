package api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static utils.TestData.*;

public class PaymentApi {
    public Response pay(int orderId, String card) {
        String body = String.format("{\"orderId\":%d,\"card\":\"%s\"}", orderId, card);
        return given().baseUri(PAYMENT_BASE_URL)
                .header("Content-Type", "application/json")
                .body(body).post(PAY_ENDPOINT);
    }
}