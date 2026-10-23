package utils;

public class TestData {

    // base urls
    public static final String STORE_BASE_URL = "https://fakestoreapi.com";
    public static final int PAYMENT_PORT = 8089;
    public static final String PAYMENT_BASE_URL = "http://localhost:" + PAYMENT_PORT;
    public static final String PAY_ENDPOINT = "/pay";

    // login (fakestoreapi)
    public static final String VALID_USER = "mor_2314";
    public static final String VALID_PASS = "83r5^_";
    public static final String INVALID_USER = "wronguser";
    public static final String INVALID_PASS = "wrongpass";

    // cards
    public static final String VALID_CARD = "4111111111111111";
    public static final String DECLINED_CARD = "4000000000000002";

    // payment response values
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";
    public static final String TXN_ID = "TXN123";
    public static final String DECLINED_REASON = "Card declined";

    // order status (used in DB checks)
    public static final String ORDER_CREATED = "CREATED";
    public static final String ORDER_CONFIRMED = "CONFIRMED";
    public static final String ORDER_PAYMENT_FAILED = "PAYMENT_FAILED";

    // order data
    public static final int USER_ID = 1;
    public static final int PRODUCT_ID = 5;
    public static final int QTY = 2;

    // http codes
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_OK = 200;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_PAYMENT_FAILED = 402;

    //ui test data
    // ui (saucedemo)
    public static final String UI_URL = "https://www.saucedemo.com";
    public static final String UI_USER = "standard_user";
    public static final String UI_LOCKED_USER = "locked_out_user";
    public static final String UI_PASS = "secret_sauce";
    public static final String UI_WRONG_PASS = "wrong_password";
    public static final String UI_PRODUCT_ID = "sauce-labs-backpack";
    public static final String UI_PRODUCT_NAME = "Sauce Labs Backpack";
    public static final String UI_PRODUCT_PRICE = "29.99";
    public static final String UI_PAYMENT_TEXT = "SauceCard";
    public static final String UI_THANK_YOU = "Thank you for your order!";
    public static final String UI_CHECKOUT_FIRST = "Test";
    public static final String UI_CHECKOUT_LAST = "User";
    public static final String UI_CHECKOUT_ZIP = "560001";
}