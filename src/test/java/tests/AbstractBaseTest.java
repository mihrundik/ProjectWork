package tests;

import config.EnvConfig;
import factory.WebDriverFactory;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;

import java.time.Duration;


public abstract class AbstractBaseTest extends AbstractBaseMethod {

    protected WebDriver driver;
    protected HeaderElPage headerElPage;
    protected LoginPage loginPage;
    protected static final Logger log = LogManager.getLogger(AbstractBaseTest.class);

//    // для отладки
//    private final String URL = "https://wishlist.otus.kartushin.su/wishlists";
//    private static final String LOGIN = System.getProperty("wishlist.login", "ИмяЛюбимоеМое2");
//    private static final String PASSWORD = System.getProperty("wishlist.password", "qwerty123");

    private final String URL = EnvConfig.getUrl();
    private static final String LOGIN = EnvConfig.getLogin();
    private static final String PASSWORD = EnvConfig.getPassword();


    @BeforeAll
    public static void startTests() {
        log.info("Начало тестирования");

        // ДОБАВЬТЕ ПРОВЕРКУ НА NULL
        if (LOGIN == null || PASSWORD == null) {
            log.error("CRITICAL: LOGIN or PASSWORD is NULL! LOGIN={}, PASSWORD={}", LOGIN, PASSWORD);
            throw new IllegalStateException("Не удалось загрузить данные авторизации. Проверьте EnvConfig.");
        }

        // проверяем, что данные для авторизации указаны
        if (LOGIN.equals("your_login") || PASSWORD.equals("your_password")) {
            log.warn("Используются данные авторизации по умолчанию. " +
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

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/wishlists"));

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


    // наличие хотя бы одного вишлиста на странице.
    protected String ensureWishlistExists(MyWishlistsPage wishlistsPage) {
        return ensureWishlistExists(wishlistsPage, null, null);
    }

    // наличие хотя бы одного вишлиста на странице с указанными параметрами.
    protected String ensureWishlistExists(MyWishlistsPage wishlistsPage,
                                          String wishlistName,
                                          String wishlistDescription) {
        if (wishlistsPage.hasWishlists()) {
            log.info("Вишлисты уже существуют. Использую существующие.");
            return wishlistsPage.getLastWishlistTitle();
        }

        log.info("Вишлисты отсутствуют. Создаю новый...");
        return createWishlist(wishlistsPage, wishlistName, wishlistDescription);
    }

    // cоздает новый вишлист с указанными параметрами.
    protected String createWishlist(MyWishlistsPage wishlistsPage,
                                    String wishlistName,
                                    String wishlistDescription) {
        String name = wishlistName != null ? wishlistName : generateWishlistName();
        String description = wishlistDescription != null ? wishlistDescription : generateWishlistDescription();

        wishlistsPage.clickAddNewList();
        wishlistsPage.waitForCreateFormToAppear();

        wishlistsPage.fillCreateForm(name, description);
        wishlistsPage.clickSubmitButton();

        wishlistsPage.waitForCreateFormToDisappear();

        log.info("Создан новый вишлист: {} с описанием: {}", name, description);
        return name;
    }

    // уникальное название для вишлиста.
    private String generateWishlistName() {
        return "Автотест-вишлист " + System.currentTimeMillis();
    }

    // описание для вишлиста по умолчанию.
    private String generateWishlistDescription() {
        return "Этот вишлист создан для автоматического теста";
    }


    public void driverStart(TestInfo testInfo) {
        String browserName = System.getProperty("browser", "edge");

        // когда значение равно "${browser}"
        if (browserName == null || browserName.isEmpty() || browserName.equals("${browser}")) {
            browserName = "edge";
            log.info("Браузер не указан, используем значение по умолчанию: edge");
        }

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
        page = new PageFactory(newDriver);
    }

    protected Capabilities getOptions(String browserName) {
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

        // если есть - парсим их
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
}