package tests.GiftPageTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Capabilities;
import pages.MyWishListPage;
import pages.AddGiftPage;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import static org.junit.jupiter.api.Assertions.*;

public class CancelAddGiftTest extends AbstractBaseTest {

    private MyWishlistsPage wishlistsPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        return getBrowserOptions(browserName);
    }

    @BeforeEach
    public void setUp() {
        wishlistsPage = new MyWishlistsPage(driver);
        wishlistsPage.open();
    }


    @Test
    @DisplayName("Тест: Проверка отмены в окне создания нового подарка")
    void testCancelAddGift() {

        // проверяем, что на странице 'Мои списки' есть хотя бы один список
        if (!wishlistsPage.hasWishlists()) {
            wishlistsPage.clickAddNewList();
            wishlistsPage.waitForCreateFormToAppear();

            String testName = "Тестовый список " + System.currentTimeMillis();
            String testDescription = "Описание тестового списка";

            wishlistsPage.fillCreateForm(testName, testDescription);
            wishlistsPage.clickSubmitButton();
            wishlistsPage.waitForCreateFormToDisappear();

            log.info("Создан тестовый список для проверки модалки: {}", testName);
        }

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