package factory.sattings;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;


public class OptionsParser {

    /**
     * Преобразует строку с параметрами запуска браузера в объект опций для конкретного браузера.
     */
    public static AbstractDriverOptions<?> parse(String browserName, String optionsString) {
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

            /**
             * Safari может не поддерживать все аргументы командной строки
             */
            case "safari":
                return new SafariOptions();

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments(optionsArray);
                return edgeOptions;

            default:
                throw new IllegalArgumentException("Неподдерживаемый браузер: " + browserName);
        }
    }
}