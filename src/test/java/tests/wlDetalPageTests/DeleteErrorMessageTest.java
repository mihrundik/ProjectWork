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


public class DeleteErrorMessageTest extends AbstractBaseTest {

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
     * 2. Переходит к последнему вишлисту
     * 3. Дважды нажимает кнопку "Удалить список" (имитация ошибки)
     * 4. Проверяет, что отображается ожидаемое сообщение об ошибке
     */
    @Test
    @DisplayName("Кнопка 'Удалить список' при ошибке показывает сообщение 'Ошибка: Ошибка при загрузке списка желаний'")
    public void deleteWishlistShowsErrorMessage() {
        // Обеспечиваем наличие вишлиста
        wishlistHelper.ensureWishlistExists();

        // Переходим к последнему вишлисту
        wishlistsPage.clickViewButtonOnLastWishlist();
        MyWishListPage wishListPage = new MyWishListPage(driver);

        // Проверяем, что страница вишлиста загрузилась
        assertTrue(wishListPage.isWishlistPageDisplayed(), "Страница вишлиста не загрузилась");

        // Нажимаем кнопку "Удалить список" дважды для имитации ошибки
        wishListPage.clickDeleteWishlistButton();
        wishListPage.clickDeleteWishlistButton();

        // Проверяем отображение сообщения об ошибке
        String expectedError = "Ошибка: Ошибка при загрузке списка желаний";
        assertTrue(wishListPage.isErrorMessageDisplayed(expectedError),
                String.format("Ожидалось сообщение об ошибке '%s', но оно не появилось", expectedError));
    }

}