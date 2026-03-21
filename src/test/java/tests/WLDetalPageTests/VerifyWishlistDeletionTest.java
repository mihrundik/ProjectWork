package tests.WLDetalPageTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;
import pages.MyWishListPage;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import factory.sattings.OptionsParser;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerifyWishlistDeletionTest extends AbstractBaseTest {

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
    @DisplayName("Проверка удаления вишлиста: количество списков уменьшается на 1")
    public void verifyWishlistDeletionReducesListCount() {
        // 1. Проверяем, что есть хотя бы один список для удаления
        assertTrue(wishlistsPage.hasWishlists(), "Нет ни одного вишлиста для проведения теста");

        // 2. Запоминаем исходное количество списков
        int initialCount = wishlistsPage.getWishlistCount();
        log.info("Исходное количество вишлистов: {}", initialCount);

        // 3. Переходим в последний список и открываем его
        wishlistsPage.clickViewButtonOnLastWishlist();
        wishListPage = new MyWishListPage(driver);

        assertTrue(wishListPage.isWishlistPageDisplayed(), "Не удалось перейти на страницу вишлиста");

        // 4. Нажимаем кнопку "Удалить список"
        wishListPage.clickDeleteWishlistButton();

        // 5. Возвращаемся на страницу "Мои списки" с помощью браузера
        driver.navigate().back();

        wishlistsPage.waitForPageToLoad();

        // переинициализируем элементы страницы, так как старый объект может содержать устаревшие данные
        wishlistsPage = new MyWishlistsPage(driver);

        // 6. Получаем новое количество списков после удаления
        int newCount = wishlistsPage.getWishlistCount();
        log.info("Новое количество вишлистов после удаления: {}", newCount);

        // 7. Проверяем, что количество элементов уменьшилось ровно на 1
        assertEquals(initialCount - 1, newCount,
                String.format("Количество списков не изменилось. Ожидалось: %d, Фактически: %d", initialCount - 1, newCount));
    }
}