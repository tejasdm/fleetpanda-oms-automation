package tests;

import base.UiBase;
import org.testng.annotations.Test;
import pages.*;

import static org.testng.Assert.*;

public class UiOrderFlowTest extends UiBase {

    @Test
    public void ui_fullFlow_success() {
        // login
        new LoginPage(driver).login("standard_users", "secret_sauce");
        ProductsPage products = new ProductsPage(driver);
        assertEquals(products.getTitle(), "Products");

        // create order: add item and check cart
        products.addToCart("sauce-labs-backpack");
        assertEquals(products.getCartCount(), "1");
        products.openCart();

        CheckoutPage checkout = new CheckoutPage(driver);
        assertEquals(checkout.getCartItemName(), "Sauce Labs Backpack");

        // checkout details
        checkout.startCheckout();
        checkout.fillDetails("Test", "User", "560001");
        checkout.clickContinue();

        // validate order summary and payment info
        assertTrue(checkout.getItemTotal().contains("29.99"), "price is wrong");
        assertTrue(checkout.getPaymentInfo().contains("SauceCard"), "payment info missing");

        // pay and confirm
        checkout.finishOrder();
        assertEquals(checkout.getConfirmationMessage(), "Thank you for your order!");
    }

    @Test
    public void ui_invalidLogin_showsError() {
        LoginPage login = new LoginPage(driver);
        login.login("standard_user", "wrong_password");
        assertTrue(login.getErrorMessage().contains("do not match"),
                "expected invalid login error");
    }

    @Test
    public void ui_lockedUser_cannotLogin() {
        LoginPage login = new LoginPage(driver);
        login.login("locked_out_user", "secret_sauce");
        assertTrue(login.getErrorMessage().contains("locked out"));
    }

    @Test
    public void ui_checkoutWithoutDetails_shouldBlockOrder() {
        new LoginPage(driver).login("standard_user", "secret_sauce");
        ProductsPage products = new ProductsPage(driver);
        products.addToCart("sauce-labs-backpack");
        products.openCart();

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.startCheckout();
        checkout.clickContinue(); // nothing filled

        assertTrue(checkout.getErrorMessage().contains("First Name is required"));
    }
}