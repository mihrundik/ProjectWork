package tests.GiftPageTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import pages.MyWishlistsPage;
import pages.MyWishListPage;
import pages.AddGiftPage;
import tests.AbstractBaseTest;

import static org.junit.jupiter.api.Assertions.*;

public class AddGiftButtonTest extends AbstractBaseTest {

    private MyWishlistsPage wishlistsPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        return super.getOptions(browserName);
    }

    @BeforeEach
    public void setUp() {
        wishlistsPage = new MyWishlistsPage(driver);
        wishlistsPage.open();
    }

    @Test
    @DisplayName("Тест: Проверка полей в окне создания нового подарка")
    void testToAddGiftPage() {

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

        // подготавливаем тестовые данные
        String testName = "Test Gift " + System.currentTimeMillis();
        String testDescription = "Описание тестового подарка для автотеста";
        String testProductUrl = "https://flowwow.com/bakery-products/tort-na-den-rozhdeniya-pr-2603/";
        String testPrice = "4300";
        String testImageUrl = "https://content2.flowwow-images.com/data/flowers/1000x1000/35/1730898908_64456035.jpg";

        // заполняем поля (очищаем перед вводом на всякий случай)
        addGiftPage.getGiftNameField().clear();
        addGiftPage.getGiftNameField().sendKeys(testName);

        addGiftPage.getGiftDescriptionField().clear();
        addGiftPage.getGiftDescriptionField().sendKeys(testDescription);

        addGiftPage.giftUrlProdact().clear();
        addGiftPage.giftUrlProdact().sendKeys(testProductUrl);

        // ставим курсор в поле цены
        addGiftPage.giftPriceProdact().click();
        // нажимаем стрелку влево несколько раз, чтобы гарантированно дойти до начала
        for (int i = 0; i < 10; i++) {
            addGiftPage.giftPriceProdact().sendKeys(Keys.ARROW_LEFT);
        }
        addGiftPage.giftPriceProdact().sendKeys(testPrice);
        addGiftPage.giftPriceProdact().sendKeys(Keys.DELETE);

        addGiftPage.giftUrlImage().clear();
        addGiftPage.giftUrlImage().sendKeys(testImageUrl);

        // проверяем, что поля содержат введённые значения (через attribute "value")
        assertEquals(testName, addGiftPage.getGiftNameField().getAttribute("value"),
                "Поле Название подарка не содержит ожидаемого значения");
        assertEquals(testDescription, addGiftPage.getGiftDescriptionField().getAttribute("value"),
                "Поле Описание подарка не содержит ожидаемого значения");
        assertEquals(testProductUrl, addGiftPage.giftUrlProdact().getAttribute("value"),
                "Поле URL продукта не содержит ожидаемого значения");
        assertEquals(testPrice, addGiftPage.giftPriceProdact().getAttribute("value"),
                "Поле Цена не содержит ожидаемого значения");
        assertEquals(testImageUrl, addGiftPage.giftUrlImage().getAttribute("value"),
                "Поле URL картинки не содержит ожидаемого значения");

        // закрываем модалку крестиком
        addGiftPage.clickCancelButton();
        assertTrue(addGiftPage.waitForModalToDisappear(), "Модальное окно не закрылось после нажатия крестика");

        // убедится, что страница вишлиста снова видима
        assertTrue(wishListPage.isWishlistPageDisplayed(), "После закрытия модалки страница вишлиста не отображается");
    }

}