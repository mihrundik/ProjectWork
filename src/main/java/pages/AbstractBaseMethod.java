package pages;

import driverAutomation.AbstractWebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;


public abstract class AbstractBaseMethod extends AbstractWebDriver {

    public Logger log = LogManager.getLogger(AbstractBaseMethod.class);
    public PageFactory page;

    /**
     * Возвращает текущий экземпляр WebDriver.
     */
    public WebDriver getCurrentDriver() {
        return super.getDriver();
    }

    /**
     * Создает объект Capabilities для указанного браузера на основе строки параметров.
     */
    public Capabilities createOptionsFromString(String browserName, String optionsString) {
        String[] optionsArray = optionsString.split(",");

        switch (browserName.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(optionsArray);
                return chromeOptions;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments(optionsArray);
                return firefoxOptions;

            case "safari":
                SafariOptions safariOptions = new SafariOptions();
                return safariOptions;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments(optionsArray);
                return edgeOptions;

            default:
                throw new IllegalArgumentException("Неподдерживаемый браузер: " + browserName);
        }
    }
}