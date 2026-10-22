package api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderApi {
    public Response createOrder(String token, int userId, int productId, int qty) {
        String body = String.format(
                "{\"userId\":%d,\"date\":\"2024-01-01\",\"products\":[{\"productId\":%d,\"quantity\":%d}]}",
                userId, productId, qty);
        return given().header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(body).post("/carts");
    }
}