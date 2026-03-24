package tests.giftPageTests;

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


    /**
     * Тест выполняет следующие шаги:
     * 1. Обеспечивает наличие хотя бы одного вишлиста
     * 2. Открывает последний вишлист
     * 3. Открывает модальное окно добавления подарка
     * 4. Нажимает кнопку отмены (крестик)
     * 5. Проверяет, что модальное окно закрылось
     * 6. Проверяет, что страница вишлиста снова отображается
     */
    @Test
    @DisplayName("Тест: Проверка отмены в окне создания нового подарка")
    void testCancelAddGift() {

        // Обеспечиваем наличие вишлиста
        wishlistHelper.ensureWishlistExists();

        // Открываем последний список
        wishlistsPage.clickViewButtonOnLastWishlist();
        MyWishListPage wishListPage = new MyWishListPage(driver);

        // Проверяем, что страница вишлиста загрузилась
        assertTrue(wishListPage.isWishlistPageDisplayed(), "Страница вишлиста не загрузилась");

        // Нажимаем кнопку "Добавить подарок"
        wishListPage.clickAddGiftButton();

        // Инициализируем объект страницы модального окна
        AddGiftPage addGiftPage = new AddGiftPage(driver);

        // Ожидаем появления модального окна
        assertTrue(addGiftPage.waitForModalToAppear(), "Модальное окно добавления подарка не появилось");

        // Проверяем, что модальное окно отображается
        assertTrue(addGiftPage.isModalDisplayed(), "Модальное окно не отображено (isModalDisplayed == false)");

        // Нажимаем кнопку отмены (крестик)
        addGiftPage.clickCancelButton();

        // Ожидаем закрытия модального окна
        assertTrue(addGiftPage.waitForModalToDisappear(), "Модальное окно не закрылось после нажатия Cancel");

        // Проверяем, что страница вишлиста снова отображается
        assertTrue(wishListPage.isWishlistPageDisplayed(), "После закрытия модального окна страница вишлиста не отображается");
    }

}