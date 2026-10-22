package api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class PaymentApi {
    public Response pay(int orderId, String card) {
        String body = String.format("{\"orderId\":%d,\"card\":\"%s\"}", orderId, card);
        return given().baseUri("http://localhost:8089")
                .header("Content-Type", "application/json")
                .body(body).post("/pay");
    }
}