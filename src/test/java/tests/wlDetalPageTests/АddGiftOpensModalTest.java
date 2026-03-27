package tests.wlDetalPageTests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishListPage;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WishlistHelper;


public class АddGiftOpensModalTest extends AbstractBaseTest {

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
     * 3. Нажимает кнопку "Добавить подарок"
     * 4. Проверяет, что открылось модальное окно с заголовком "Добавить подарок"
     */
    @Test
    @DisplayName("Кнопка 'Добавить подарок' открывает модальное окно с заголовком 'Добавить подарок'")
    public void addGiftButtonOpensModal() {

        // Обеспечиваем наличие вишлиста
        wishlistHelper.ensureWishlistExists();

        // Переходим к последнему вишлисту
        wishlistsPage.clickViewButtonOnLastWishlist();
        MyWishListPage wishListPage = new MyWishListPage(driver);

        // Проверяем, что страница вишлиста загрузилась
        wishListPage.assertions().verifyWishlistPageLoaded();

        // Проверяем, что кнопка кликабельна перед нажатием
        wishListPage.assertions().verifyAddGiftButtonClickable();

        // Нажимаем кнопку "Добавить подарок"
        wishListPage.clickAddGiftButton();

        // Проверяем, что открылось модальное окно с правильным заголовком
        wishListPage.assertions().verifyAddGiftModalOpened();
    }

}