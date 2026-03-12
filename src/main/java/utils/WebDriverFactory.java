package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;


public class WebDriverFactory {

    public static WebDriver create(String browserName, Capabilities options) {
        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                return initChromeDriver(options);

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return initFirefoxDriver(options);

            case "safari":
                WebDriverManager.safaridriver().setup();
                return initSafariDriver(options);

            case "edge":
                WebDriverManager.edgedriver().setup();
                return initEdgeDriver(options);

            default:
                throw new IllegalArgumentException("Неподдерживаемое имя браузера: " + browserName);
        }
    }

    private static WebDriver initChromeDriver(Capabilities options) {
        if (options instanceof ChromeOptions) {
            return new ChromeDriver((ChromeOptions) options);
        } else {
            ChromeOptions chromeOptions = new ChromeOptions();
            options.asMap().forEach(chromeOptions::setCapability);
            return new ChromeDriver(chromeOptions);
        }
    }

    private static WebDriver initFirefoxDriver(Capabilities options) {
        if (options instanceof FirefoxOptions) {
            return new FirefoxDriver((FirefoxOptions) options);
        } else {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            options.asMap().forEach(firefoxOptions::setCapability);
            return new FirefoxDriver(firefoxOptions);
        }
    }

    private static WebDriver initSafariDriver(Capabilities options) {
        if (options instanceof SafariOptions) {
            return new SafariDriver((SafariOptions) options);
        } else {
            SafariOptions safariOptions = new SafariOptions();
            options.asMap().forEach(safariOptions::setCapability);
            return new SafariDriver(safariOptions);
        }
    }

    private static WebDriver initEdgeDriver(Capabilities options) {
        if (options instanceof EdgeOptions) {
            return new EdgeDriver((EdgeOptions) options);
        } else {
            EdgeOptions edgeOptions = new EdgeOptions();
            options.asMap().forEach(edgeOptions::setCapability);
            return new EdgeDriver(edgeOptions);
        }
    }
}