package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.Map;

import static utils.TestData.UI_URL;

public class UiBase {
    protected WebDriver driver;

    @BeforeMethod
    public void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        // run headless in CI: mvn test -Dheadless=true
        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        options.addArguments("--disable-notifications");
        options.addArguments("--password-store=basic");
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.get(UI_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver != null) driver.quit();
    }
}