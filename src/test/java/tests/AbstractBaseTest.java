package tests;

import config.EnvConfig;
import factory.sattings.OptionsParser;
import org.junit.jupiter.api.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.safari.SafariOptions;
import pages.*;
import factory.WebDriverFactory;
import utils.WaitUtils;

import java.time.Duration;


public abstract class AbstractBaseTest extends AbstractBaseMethod {

    protected WebDriver driver;
    protected static final Logger log = LogManager.getLogger(AbstractBaseTest.class);
    private final String URL = EnvConfig.getUrl();

    // данные для авторизации (потом будет через system properties)
    private static final String LOGIN = EnvConfig.getLogin();
    private static final String PASSWORD = EnvConfig.getPassword();

    // страницы, которые могут понадобиться в тестах
    protected HeaderElPage headerElPage;
    protected LoginPage loginPage;


    @BeforeAll
    public static void startTests() {
        log.info("Начало тестирования");

        // проверяем, что данные для авторизации указаны
        if (LOGIN.equals("your_login") || PASSWORD.equals("your_password")) {
            log.warn("Используются данные авторизации по умолчанию. " +
                    // на будущее для запуска через командную строку
                    "Рекомендуется указать свои через -Dwishlist.login и -Dwishlist.password");
        }
    }

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        driverStart(testInfo);
        this.driver = getCurrentDriver();

        // очистка состояния для изоляции тестов
        driver.manage().deleteAllCookies();
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear(); window.sessionStorage.clear();");

        // Инициализация page/страниц
        page = new PageFactory(driver);
        headerElPage = page.header;
        loginPage = new LoginPage(driver);

        // явная авторизация
        driver.get(URL);
        loginPage.login(LOGIN, PASSWORD);

        boolean isRedirected = WaitUtils.waitForCondition(driver,
                webDriver -> webDriver.getCurrentUrl().contains("/wishlists"),
                Duration.ofSeconds(5)
        );

        log.info("Авторизация выполнена успешно. Тест готов к запуску.");
    }

    @AfterEach
    public void driverClose() {
        if (driver != null) {
            driver.quit();
        }
        setDriver(null);
        this.driver = null;
    }

    @AfterAll
    public static void endTests() {
        log.info("Конец тестирования\n");
    }

    // авторизация
    private void performLogin() {
        try {
            log.info("Проверка необходимости авторизации");

            String currentUrl = driver.getCurrentUrl();

            // если уже на странице списков - авторизация не нужна
            if (currentUrl.contains("/wishlists")) {
                log.info("Уже на странице списков желаний, авторизация не требуется");
                return;
            }

            // переходим на страницу логина через хедер
            log.info("Переходим на страницу авторизации");
            headerElPage.goToMyLists();

            // проверяем, что мы на странице логина
            if (loginPage.isLoginPageDisplayed()) {
                // Выполняем вход
                log.info("Выполняем вход в систему с логином: {}", LOGIN);
                loginPage.login(LOGIN, PASSWORD);

                // небольшая задержка для завершения авторизации
                boolean isRedirected = WaitUtils.waitForCondition(driver,
                        webDriver -> webDriver.getCurrentUrl().contains("/wishlists"),
                        Duration.ofSeconds(5)
                );

                log.info("Вход выполнен успешно");
            } else {
                log.error("Не удалось перейти на страницу логина");
                throw new RuntimeException("Страница логина не отображается");
            }

        } catch (Exception e) {
            log.error("Ошибка при авторизации: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось выполнить авторизацию", e);
        }
    }


    protected Capabilities getBrowserOptions(String browserName) {
        // проверяем опции в командной строке
        String optionsFromCmd = null;
        switch (browserName.toLowerCase()) {
            case "chrome":
                optionsFromCmd = System.getProperty("chromeOptions");
                break;
            case "firefox":
                optionsFromCmd = System.getProperty("firefoxOptions");
                break;
            case "safari":
                optionsFromCmd = System.getProperty("safariOptions");
                break;
            case "edge":
                optionsFromCmd = System.getProperty("edgeOptions");
                break;
        }

        // если есть опции из командной строки - парсим их
        if (optionsFromCmd != null && !optionsFromCmd.isEmpty()) {
            return OptionsParser.parse(browserName, optionsFromCmd);
        }

        // или используем стандартные опции
        switch (browserName.toLowerCase()) {
            case "chrome":
                return new ChromeOptions();
            case "firefox":
                return new FirefoxOptions();
            case "safari":
                return new SafariOptions();
            case "edge":
                return new EdgeOptions();
            default:
                throw new IllegalArgumentException("Неподдерживаемый браузер: " + browserName);
        }
    }

    // оставляем абстрактный метод для возможности переопределения
    protected abstract Capabilities getOptions(String browserName);

    public void driverStart(TestInfo testInfo) {
        String browserName = System.getProperty("browser", "chrome");

        // получаем опции из метода, который может быть переопределен в дочернем классе
        Capabilities options = getBrowserOptions(browserName);

        log.info("Запуск теста: {} в браузере {}:\n{}",
                testInfo.getDisplayName(), browserName, options);

        // создаем параметризированный драйвер
        WebDriver newDriver = WebDriverFactory.create(browserName, (AbstractDriverOptions<?>) options);

        // устанавливаем параметризированный драйвер
        setDriver(newDriver);

        newDriver.get(URL);
        page = new PageFactory(newDriver);
    }

}