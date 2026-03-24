package driverAutomation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;


public abstract class AbstractWebDriver {

    private static final Logger log = LoggerFactory.getLogger(AbstractWebDriver.class);

    protected WebDriver driver;
    protected Actions actions;
    protected WebDriverWait wait;

    /**
     * Конструктор инициализации компонентов не производится, так как WebDriver еще не установлен.
     */
    public AbstractWebDriver() {
        log.debug("Создан экземпляр AbstractWebDriver (драйвер не инициализирован)");
    }

    /**
     * Устанавливает WebDriver и инициализирует вспомогательные компоненты.
     */
    public void setDriver(WebDriver driver) {
        this.driver = driver;

        if (driver != null) {
            this.actions = new Actions(driver);
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(1));
            log.debug("WebDriver установлен. Инициализированы Actions и WebDriverWait (таймаут: 1 сек)");
        } else {
            log.warn("Попытка установить null в качестве WebDriver");
        }
    }

    /**
     * Возвращает текущий экземпляр WebDriver.
     */
    public WebDriver getDriver() {
        log.debug("Возвращен WebDriver: {}", driver != null ? "инициализирован" : "null");
        return this.driver;
    }

    /**
     * Проверяет, инициализирован ли WebDriver.
     */
    public boolean isDriverInitialized() {
        return driver != null;
    }
}