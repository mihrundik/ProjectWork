package tests.wlDetalPageTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishListPage;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WishlistHelper;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class VerifyWishlistDeletionTest extends AbstractBaseTest {

    private MyWishlistsPage wishlistsPage;
    private WishlistHelper wishlistHelper;

    @Override
    protected Capabilities getOptions(String browserName) {
        return super.getOptions(browserName);
    }

    @BeforeEach
    public void setUp() {
        wishlistsPage = new MyWishlistsPage(driver);
        wishlistHelper = new WishlistHelper(driver);
        wishlistsPage.open();
    }


    /**
     * Тест выполняет следующие шаги:
     * 1. Обеспечивает наличие хотя бы одного вишлиста
     * 2. Сохраняет исходное количество списков
     * 3. Переходит к последнему вишлисту
     * 4. Нажимает кнопку "Удалить список"
     * 5. Возвращается на страницу со списком вишлистов
     * 6. Проверяет, что количество списков уменьшилось на 1
     */
    @Test
    @DisplayName("Проверка удаления вишлиста: количество списков уменьшается на 1")
    public void verifyWishlistDeletionReducesListCount() {

        // Обеспечиваем наличие вишлиста
        wishlistHelper.ensureWishlistExists();

        // Проверяем, что есть хотя бы один список для удаления
        assertTrue(wishlistsPage.hasWishlists(), "Нет ни одного вишлиста для проведения теста");

        // Сохраняем исходное количество списков
        int initialCount = wishlistsPage.getWishlistCount();
        log.info("Исходное количество вишлистов: {}", initialCount);

        // Переходим в последний список
        wishlistsPage.clickViewButtonOnLastWishlist();
        MyWishListPage wishListPage = new MyWishListPage(driver);

        // Проверяем успешный переход на страницу вишлиста
        wishListPage.verifyWishlistPageLoaded();

        // Проверяем, что кнопка "Удалить список" кликабельна
        wishListPage.verifyDeleteWishlistButtonClickable();

        // Нажимаем кнопку "Удалить список"
        wishListPage.clickDeleteWishlistButton();

        // Возвращаемся на страницу со списком вишлистов
        driver.navigate().back();

        // Ожидаем загрузки страницы и проверяем, что количество списков уменьшилось
        wishlistsPage.waitForPageToLoad();

        // Переинициализируем страницу и проверяем результат
        wishlistsPage = new MyWishlistsPage(driver);
        wishlistsPage.verifyWishlistDeleted(initialCount);
    }

}