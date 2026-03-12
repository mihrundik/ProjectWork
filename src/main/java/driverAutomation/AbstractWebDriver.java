package driverAutomation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public abstract class AbstractWebDriver {

    protected WebDriver driver;
    protected Actions actions;
    protected WebDriverWait wait;

    public AbstractWebDriver() {
        // не инициализируем здесь, так как driver еще null
    }

    public AbstractWebDriver(WebDriver driver) {
        setDriver(driver);
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
        if (driver != null) {
            this.actions = new Actions(driver);
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        }
    }

    public WebDriver getDriver() {
        return this.driver;
    }

}