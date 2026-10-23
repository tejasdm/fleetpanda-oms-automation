package base;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static utils.TestData.*;

public class BaseTest {
    protected static WireMockServer mock;

    @BeforeSuite
    public void setup() {
        RestAssured.baseURI = STORE_BASE_URL;

        // mock payment service
        mock = new WireMockServer(PAYMENT_PORT);
        mock.start();

        // good card -> success
        mock.stubFor(post(PAY_ENDPOINT).withRequestBody(containing(VALID_CARD))
                .willReturn(okJson(String.format(
                        "{\"status\":\"%s\",\"txnId\":\"%s\"}", STATUS_SUCCESS, TXN_ID))));

        // bad card -> declined
        mock.stubFor(post(PAY_ENDPOINT).withRequestBody(containing(DECLINED_CARD))
                .willReturn(aResponse().withStatus(HTTP_PAYMENT_FAILED)
                        .withHeader("Content-Type", "application/json")
                        .withBody(String.format(
                                "{\"status\":\"%s\",\"reason\":\"%s\"}", STATUS_FAILED, DECLINED_REASON))));
    }

    @AfterSuite
    public void tearDown() { mock.stop(); }
}