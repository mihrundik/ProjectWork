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


    @Test
    @DisplayName("Кнопка 'Удалить список' при ошибке показывает сообщение 'Ошибка: Ошибка при загрузке списка желаний'")
    public void deleteWishlistShowsErrorMessage() {
        // Шаг 1: проверяем наличие списков на уже загруженной странице
        // хелпер для обеспечения наличия вишлиста
        wishlistHelper.ensureWishlistExists();

        wishlistsPage.clickViewButtonOnLastWishlist();
        MyWishListPage wishListPage = new MyWishListPage(driver);

        // проверяем, что страница вишлиста загрузилась
        assertTrue(wishListPage.isWishlistPageDisplayed(), "Страница вишлиста не загрузилась");

        // нажимаем "Удалить список"
        wishListPage.clickDeleteWishlistButton();

        // нажимаем "Удалить список"
        wishListPage.clickDeleteWishlistButton();

        // ожидаем сообщения об ошибке в root/div
        String expectedError = "Ошибка: Ошибка при загрузке списка желаний";
        assertTrue(wishListPage.isErrorMessageDisplayed(expectedError),
                String.format("Ожидалось сообщение об ошибке '%s', но оно не появилось", expectedError));
    }

}