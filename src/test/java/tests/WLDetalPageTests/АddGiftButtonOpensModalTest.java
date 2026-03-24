package tests.WLDetalPageTests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishListPage;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WishlistHelper;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class АddGiftButtonOpensModalTest extends AbstractBaseTest {

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
    @DisplayName("Кнопка 'Добавить подарок' открывает модальное окно с заголовком 'Добавить подарок'")
    public void addGiftButtonOpensModal() {

        // Шаг 1: проверяем наличие списков на уже загруженной странице
        // хелпер для обеспечения наличия вишлиста
        wishlistHelper.ensureWishlistExists();

        wishlistsPage.clickViewButtonOnLastWishlist();
        wishListPage = new MyWishListPage(driver);

        // проверяем, что страница вишлиста загрузилась
        assertTrue(wishListPage.isWishlistPageDisplayed(), "Страница вишлиста не загрузилась");

        // нажимаем кнопку "Добавить подарок"
        wishListPage.clickAddGiftButton();

        // ожидаем появления модального окна с заголовком "Добавить подарок"
        assertTrue(wishListPage.isAddGiftModalDisplayedWithTitle("Добавить подарок"),
                "Модальное окно 'Добавить подарок' не появилось или заголовок не совпадает");
    }

}