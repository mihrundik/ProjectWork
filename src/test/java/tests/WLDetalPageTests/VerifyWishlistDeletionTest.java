package tests.WLDetalPageTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishListPage;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WishlistHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class VerifyWishlistDeletionTest extends AbstractBaseTest {

    private MyWishlistsPage wishlistsPage;
    private MyWishListPage wishListPage;
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


    @Test
    @DisplayName("Проверка удаления вишлиста: количество списков уменьшается на 1")
    public void verifyWishlistDeletionReducesListCount() {

        // Шаг 1: проверяем наличие списков на уже загруженной странице
        // хелпер для обеспечения наличия вишлиста
        wishlistHelper.ensureWishlistExists();

        // 1. проверяем, что есть хотя бы один список для удаления
        assertTrue(wishlistsPage.hasWishlists(), "Нет ни одного вишлиста для проведения теста");

        // 2. запоминаем исходное количество списков
        int initialCount = wishlistsPage.getWishlistCount();
        log.info("Исходное количество вишлистов: {}", initialCount);

        // 3. переходим в последний список и открываем его
        wishlistsPage.clickViewButtonOnLastWishlist();
        wishListPage = new MyWishListPage(driver);

        assertTrue(wishListPage.isWishlistPageDisplayed(), "Не удалось перейти на страницу вишлиста");

        // 4. нажимаем кнопку "Удалить список"
        wishListPage.clickDeleteWishlistButton();

        // 5. возвращаемся на страницу "Мои списки" с помощью браузера
        driver.navigate().back();

        wishlistsPage.waitForPageToLoad();

        // переинициализируем элементы страницы, так как старый объект может содержать устаревшие данные
        wishlistsPage = new MyWishlistsPage(driver);

        // 6. получаем новое количество списков после удаления
        int newCount = wishlistsPage.getWishlistCount();
        log.info("Новое количество вишлистов после удаления: {}", newCount);

        // 7. проверяем, что количество элементов уменьшилось ровно на 1
        assertEquals(initialCount - 1, newCount,
                String.format("Количество списков не изменилось. Ожидалось: %d, Фактически: %d", initialCount - 1, newCount));
    }
}