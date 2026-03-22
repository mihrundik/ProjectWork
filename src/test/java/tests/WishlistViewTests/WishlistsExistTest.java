package tests.WishlistViewTests;

import factory.sattings.OptionsParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WaitUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WishlistsExistTest extends AbstractBaseTest {

    private MyWishlistsPage wishlistsPage;

    @Override
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

    @BeforeEach
    public void setUp() {
        wishlistsPage = new MyWishlistsPage(driver);
        wishlistsPage.open();
    }


    @Test
    @DisplayName("Тест: Проверка наличия списков желаний")
    void testWishlistsExist() {
        // Шаг 1: проверяем наличие списков на уже загруженной странице
        if (!wishlistsPage.hasWishlists()) {
            log.info("Списки желаний отсутствуют. Создаём новый...");

            // Шаг 1.1: вызываем метод на существующем объекте страницы.
            wishlistsPage.clickAddNewList();

            // ждём появления формы (используем улучшенный метод)
            wishlistsPage.waitForCreateFormToAppear(); // Теперь это работает корректно!

            String tempWishlistName = "Автотест-вишлист " + System.currentTimeMillis();
            String tempWishlistDesc = "Этот вишлист создан для автоматического теста";

            wishlistsPage.fillCreateForm(tempWishlistName, tempWishlistDesc);
            wishlistsPage.clickSubmitButton();

            // ждём исчезновение формы
            wishlistsPage.waitForCreateFormToDisappear();
            log.info("Создан временный вишлист: {}", tempWishlistName);
        }

        // Шаг 2: проверка наличия вишлистов
        assertTrue(wishlistsPage.hasWishlists(),
                "Должен быть хотя бы один список желаний");

        log.info("Тест пройден успешно.");
    }
}