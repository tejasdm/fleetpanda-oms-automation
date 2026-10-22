package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class UiBase {
    protected WebDriver driver;
    protected static final String URL = "https://www.saucedemo.com";

    @BeforeMethod
    public void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        // run headless in CI: mvn test -Dheadless=true
        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        options.addArguments("--disable-notifications");
        // stops chrome showing the "change your password" popup
        options.addArguments("--password-store=basic");
        options.setExperimentalOption("prefs", java.util.Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // using explicit waits only
        driver.get(URL);
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver != null) driver.quit();
    }
}