package api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class LoginApi {
    public Response login(String user, String pass) {
        String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", user, pass);
        return given().header("Content-Type", "application/json")
                .body(body).post("/auth/login");
    }

    public String getToken(String user, String pass) {
        return login(user, pass).jsonPath().getString("token");
    }
}