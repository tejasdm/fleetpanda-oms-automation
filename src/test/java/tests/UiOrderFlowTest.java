package tests;

import base.UiBase;
import org.testng.annotations.Test;
import pages.*;

import static org.testng.Assert.*;
import static utils.TestData.*;

public class UiOrderFlowTest extends UiBase {

    @Test
    public void ui_fullFlow_success() {
        // login
        LoginPage login = new LoginPage(driver);
        login.login(UI_USER, UI_PASS);
        ProductsPage products = new ProductsPage(driver);
        assertEquals(products.getTitle(), "Products");

        // create order: add item and check cart
        products.addToCart(UI_PRODUCT_ID);
        assertEquals(products.getCartCount(), "1");
        products.openCart();

        CheckoutPage checkout = new CheckoutPage(driver);
        assertEquals(checkout.getCartItemName(), UI_PRODUCT_NAME);

        // checkout details
        checkout.startCheckout();
        checkout.fillDetails(UI_CHECKOUT_FIRST, UI_CHECKOUT_LAST, UI_CHECKOUT_ZIP);
        checkout.clickContinue();

        // validate order summary and payment info
        assertTrue(checkout.getItemTotal().contains(UI_PRODUCT_PRICE), "price is wrong");
        assertTrue(checkout.getPaymentInfo().contains(UI_PAYMENT_TEXT), "payment info missing");

        // pay and confirm
        checkout.finishOrder();
        assertEquals(checkout.getConfirmationMessage(), UI_THANK_YOU);
    }

    @Test
    public void ui_invalidLogin_showsError() {
        LoginPage login = new LoginPage(driver);
        login.login(UI_USER, UI_WRONG_PASS);
        assertTrue(login.getErrorMessage().contains("do not match"),
                "expected invalid login error");
    }

    @Test
    public void ui_lockedUser_cannotLogin() {
        LoginPage login = new LoginPage(driver);
        login.login(UI_LOCKED_USER, UI_PASS);
        assertTrue(login.getErrorMessage().contains("locked out"));
    }

    @Test
    public void ui_checkoutWithoutDetails_shouldBlockOrder() {
        new LoginPage(driver).login(UI_USER, UI_PASS);
        ProductsPage products = new ProductsPage(driver);
        products.addToCart(UI_PRODUCT_ID);
        products.openCart();

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.startCheckout();
        checkout.clickContinue();

        assertTrue(checkout.getErrorMessage().contains("First Name is required"));
    }
}