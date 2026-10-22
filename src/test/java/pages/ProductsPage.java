package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class ProductsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By title = By.className("title");
    private By cartIcon = By.className("shopping_cart_link");
    private By cartBadge = By.className("shopping_cart_badge");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(title)).getText();
    }

    // productId example: "sauce-labs-backpack"
    public void addToCart(String productId) {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("add-to-cart-" + productId))).click();
    }

    public String getCartCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge)).getText();
    }

    public void openCart() {
        driver.findElement(cartIcon).click();
    }
}