package base;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.testng.annotations.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class BaseTest {
    protected static WireMockServer mock;

    @BeforeSuite
    public void setup() {
        RestAssured.baseURI = "https://fakestoreapi.com";

        // mock payment service
        mock = new WireMockServer(8089);
        mock.start();
        // good card -> success
        mock.stubFor(post("/pay").withRequestBody(containing("4111111111111111"))
                .willReturn(okJson("{\"status\":\"SUCCESS\",\"txnId\":\"TXN123\"}")));
        // bad card -> declined
        mock.stubFor(post("/pay").withRequestBody(containing("4000000000000002"))
                .willReturn(aResponse().withStatus(402)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"FAILED\",\"reason\":\"Card declined\"}")));
    }

    @AfterSuite
    public void tearDown() { mock.stop(); }
}