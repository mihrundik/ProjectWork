package tests.WLDetalPageTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;
import pages.MyWishListPage;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.OptionsParser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WishlistNavigationTest extends AbstractBaseTest {

    private MyWishlistsPage wishlistsPage;
    private MyWishListPage wishListPage;

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
    @DisplayName("Проверка соответствия названия списка при переходе по кнопке Просмотр")
    public void verifyWishlistTitleAfterNavigation() {
        // создаем новый список, если списков нет
        if (!wishlistsPage.hasWishlists()) {
            wishlistsPage.clickAddNewList();
            wishlistsPage.waitForCreateFormToAppear();

            String testName = "Тестовый список " + System.currentTimeMillis();
            String testDescription = "Описание тестового списка";

            wishlistsPage.fillCreateForm(testName, testDescription);
            wishlistsPage.clickSubmitButton();
            wishlistsPage.waitForCreateFormToDisappear();

            log.info("Создан тестовый список: {}", testName);
        }

        // Шаг 1: получаем название последнего списка на странице списков по которому будем кликать
        String expectedTitle = wishlistsPage.getLastWishlistTitle();
        String expectedDescription = wishlistsPage.getLastWishlistDescription();

        log.info("Название списка на странице 'Мои списки': {}", expectedTitle);
        log.info("Описание списка на странице 'Мои списки': {}", expectedDescription);

        // Шаг 2: нажимаем кнопку "Просмотр" для последнего списка
        wishlistsPage.clickViewButtonOnLastWishlist();

        // Шаг 3: инициализируем страницу конкретного вишлиста
        wishListPage = new MyWishListPage(driver);

        // Шаг 4: проверяем, что страница вишлиста загрузилась
        assertTrue(wishListPage.isWishlistPageDisplayed(),
                "Страница вишлиста не загрузилась");

        // Шаг 5: получаем фактическое название на странице вишлиста
        String actualTitle = wishListPage.getWishlistTitle();
        String actualDescription = wishListPage.getWishlistDescription();

        log.info("Название списка на странице вишлиста: {}", actualTitle);
        log.info("Описание списка на странице вишлиста: {}", actualDescription);

        // Шаг 6: сравниваем названия
        assertEquals(actualTitle, expectedTitle,
                String.format("Название списка не совпадает. Ожидалось: '%s', Фактически: '%s'",
                        expectedTitle, actualTitle));

        // Шаг 7: сравниваем описания (опционально)
        assertEquals(actualDescription, expectedDescription,
                String.format("Описание списка не совпадает. Ожидалось: '%s', Фактически: '%s'",
                        expectedDescription, actualDescription));


    }
}