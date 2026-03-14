package tests;

import org.junit.jupiter.api.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;
import pages.*;
import utils.WebDriverFactory;


public abstract class AbstractBaseTest extends AbstractBaseMethod {

    protected WebDriver driver;
    protected static final Logger log = LogManager.getLogger(AbstractBaseTest.class);
    private final String URL = "https://wishlist.otus.kartushin.su/wishlists";

    // данные для авторизации (потом будет через system properties)
    private static final String LOGIN = System.getProperty("wishlist.login", "ИмяЛюбимоеМое2");
    private static final String PASSWORD = System.getProperty("wishlist.password", "qwerty123");

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

        headerElPage = new HeaderElPage(driver);
        loginPage = new LoginPage(driver);
        page = new PageFactor(driver);

        performLogin();
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

            // Если уже на странице списков - авторизация не нужна
            if (currentUrl.contains("/wishlists")) {
                log.info("Уже на странице списков желаний, авторизация не требуется");
                return;
            }

            // Переходим на страницу логина через хедер
            log.info("Переходим на страницу авторизации");
            headerElPage.goToMyLists();

            // Проверяем, что мы на странице логина
            if (loginPage.isLoginPageDisplayed()) {
                // Выполняем вход
                log.info("Выполняем вход в систему с логином: {}", LOGIN);
                loginPage.login(LOGIN, PASSWORD);

                // Небольшая задержка для завершения авторизации
                Thread.sleep(1000);

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

    public void driverStart(TestInfo testInfo) {
        String browserName = System.getProperty("browser", "chrome");

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

        // получаем опции из метода, который может быть переопределен в дочернем классе
        Capabilities options = getOptions(browserName);

        // если есть опции из командной строки - создаем новые опции
        if (optionsFromCmd != null && !optionsFromCmd.isEmpty()) {
            options = createOptionsFromString(browserName, optionsFromCmd);
        }

        log.info("Запуск теста: {} в браузере {}:\n{}",
                testInfo.getDisplayName(), browserName, options);

        // создаем параметризированный драйвер
        WebDriver newDriver = WebDriverFactory.create(browserName, (AbstractDriverOptions<?>) options);

        // устанавливаем параметризированный драйвер
        setDriver(newDriver);

        newDriver.get(URL);
        page = new PageFactor(newDriver);
    }


    protected abstract Capabilities getOptions(String browserName);
}