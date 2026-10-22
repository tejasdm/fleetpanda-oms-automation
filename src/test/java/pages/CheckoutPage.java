package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By cartItemName = By.className("inventory_item_name");
    private By checkoutBtn = By.id("checkout");
    private By firstName = By.id("first-name");
    private By lastName = By.id("last-name");
    private By postalCode = By.id("postal-code");
    private By continueBtn = By.id("continue");
    private By finishBtn = By.id("finish");
    private By error = By.cssSelector("[data-test='error']");
    private By itemTotal = By.className("summary_subtotal_label");
    private By paymentInfo = By.xpath("//div[text()='Payment Information:']/following-sibling::div[1]");
    private By confirmHeader = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getCartItemName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartItemName)).getText();
    }

    public void startCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutBtn)).click();
    }

    public void fillDetails(String first, String last, String zip) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName)).sendKeys(first);
        driver.findElement(lastName).sendKeys(last);
        driver.findElement(postalCode).sendKeys(zip);
    }

    public void clickContinue() {
        driver.findElement(continueBtn).click();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(error)).getText();
    }

    public String getItemTotal() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(itemTotal)).getText();
    }

    public String getPaymentInfo() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(paymentInfo)).getText();
    }

    public void finishOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(finishBtn)).click();
    }

    public String getConfirmationMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmHeader)).getText();
    }
}