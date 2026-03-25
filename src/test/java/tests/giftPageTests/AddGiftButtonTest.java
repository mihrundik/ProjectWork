package tests.giftPageTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import pages.MyWishlistsPage;
import pages.MyWishListPage;
import pages.AddGiftPage;
import tests.AbstractBaseTest;
import utils.WishlistHelper;


public class AddGiftButtonTest extends AbstractBaseTest {

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
     * 4. Заполняет все поля формы
     * 5. Проверяет корректность заполнения полей
     * 6. Закрывает модальное окно
     */
    @Test
    @DisplayName("Тест: Проверка полей в окне создания нового подарка")
    void testToAddGiftPage() {
        // Обеспечиваем наличие вишлиста
        wishlistHelper.ensureWishlistExists();

        // Открываем последний список
        wishlistsPage.clickViewButtonOnLastWishlist();
        MyWishListPage wishListPage = new MyWishListPage(driver);

        // Проверяем загрузку страницы вишлиста
        wishListPage.verifyWishlistPageLoaded();

        // Нажимаем кнопку "Добавить подарок"
        wishListPage.clickAddGiftButton();

        // Инициализируем объект страницы модального окна
        AddGiftPage addGiftPage = new AddGiftPage(driver);

        // Проверяем появление модального окна
        addGiftPage.verifyModalDisplayed();

        // Подготавливаем тестовые данные
        String testName = "Test Gift " + System.currentTimeMillis();
        String testDescription = "Описание тестового подарка для автотеста";
        String testProductUrl = "https://flowwow.com/bakery-products/tort-na-den-rozhdeniya-pr-2603/";
        String testPrice = "4300";
        String testImageUrl = "https://content2.flowwow-images.com/data/flowers/1000x1000/35/1730898908_64456035.jpg";

        // Заполняем поле названия подарка
        addGiftPage.getGiftNameField().clear();
        addGiftPage.getGiftNameField().sendKeys(testName);

        // Заполняем поле описания подарка
        addGiftPage.getGiftDescriptionField().clear();
        addGiftPage.getGiftDescriptionField().sendKeys(testDescription);

        // Заполняем поле URL продукта
        addGiftPage.giftUrlProdact().clear();
        addGiftPage.giftUrlProdact().sendKeys(testProductUrl);

        // Заполняем поле цены (с особым подходом для числового поля)
        addGiftPage.giftPriceProdact().click();
        // Нажимаем стрелку влево несколько раз, чтобы гарантированно дойти до начала
        for (int i = 0; i < 10; i++) {
            addGiftPage.giftPriceProdact().sendKeys(Keys.ARROW_LEFT);
        }
        addGiftPage.giftPriceProdact().sendKeys(testPrice);
        addGiftPage.giftPriceProdact().sendKeys(Keys.DELETE);

        // Заполняем поле URL изображения
        addGiftPage.giftUrlImage().clear();
        addGiftPage.giftUrlImage().sendKeys(testImageUrl);

        // Проверяем корректность заполнения всех полей
        addGiftPage.verifyGiftFormData(testName, testDescription, testProductUrl, testPrice, testImageUrl);

        // Закрываем модальное окно крестиком
        addGiftPage.clickCancelButton();
        addGiftPage.verifyModalClosed();

        // Убеждаемся, что страница вишлиста снова видима
        wishListPage.verifyWishlistPageDisplayedAfterModalClosed();
    }
}