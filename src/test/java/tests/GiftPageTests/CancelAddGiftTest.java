package tests.GiftPageTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import pages.MyWishListPage;
import pages.AddGiftPage;
import tests.AbstractBaseTest;
import utils.WishlistHelper;

import static org.junit.jupiter.api.Assertions.*;


public class CancelAddGiftTest extends AbstractBaseTest {

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
    @DisplayName("Тест: Проверка отмены в окне создания нового подарка")
    void testCancelAddGift() {

        // Шаг 1: проверяем наличие списков на уже загруженной странице
        // хелпер для обеспечения наличия вишлиста
        wishlistHelper.ensureWishlistExists();

        // открываем последний список
        wishlistsPage.clickViewButtonOnLastWishlist();
        MyWishListPage wishListPage = new MyWishListPage(driver);

        // проверяем, что страница вишлиста загрузилась
        assertTrue(wishListPage.isWishlistPageDisplayed(), "Страница вишлиста не загрузилась");

        // нажимаем кнопку "Добавить подарок"
        wishListPage.clickAddGiftButton();

        // инициализируем объект страницы модалки
        AddGiftPage addGiftPage = new AddGiftPage(driver);

        // ждём появления модального окна
        assertTrue(addGiftPage.waitForModalToAppear(), "Модальное окно добавления подарка не появилось");

        // проверяем, что модальное окно отображается
        assertTrue(addGiftPage.isModalDisplayed(), "Модальное окно не отображено (isModalDisplayed == false)");

        // нажимаем крестик - отмену
        addGiftPage.clickCancelButton();

        // ждём закрытия модального окна
        assertTrue(addGiftPage.waitForModalToDisappear(), "Модальное окно не закрылось после нажатия Cancel");

        // проверяем, что мы снова видим страницу вишлиста - заголовок и описание
        assertTrue(wishListPage.isWishlistPageDisplayed(), "После закрытия модального окна страница вишлиста не отображается");

    }

}