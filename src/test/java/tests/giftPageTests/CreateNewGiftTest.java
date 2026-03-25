package tests.giftPageTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import pages.MyWishListPage;
import pages.AddGiftPage;
import tests.AbstractBaseTest;
import utils.WaitUtils;
import utils.WishlistHelper;


public class CreateNewGiftTest extends AbstractBaseTest {

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
     * 3. Сохраняет начальное количество подарков
     * 4. Открывает модальное окно добавления подарка
     * 5. Заполняет форму данными тестового подарка
     * 6. Сохраняет подарок
     * 7. Проверяет, что количество подарков увеличилось на 1
     */
    @Test
    @DisplayName("Создание нового подарка в вишлисте")
    void createNewGiftTest() {
        // Обеспечиваем наличие вишлиста
        wishlistHelper.ensureWishlistExists();

        // Переходим к последнему вишлисту
        wishlistsPage.clickViewButtonOnLastWishlist();
        MyWishListPage wishListPage = new MyWishListPage(driver);

        // Ожидаем загрузки страницы вишлиста
        WaitUtils.waitForVisibility(driver, By.xpath("//*[@id='root']/div/h2"));

        // Проверяем, что страница вишлиста загрузилась
        wishListPage.verifyWishlistPageLoaded();

        // Получаем начальное количество подарков
        int initialGiftCount = wishListPage.getGiftItemsCount();
        log.info("Начальное количество подарков: {}", initialGiftCount);

        // Нажимаем кнопку "Добавить подарок"
        wishListPage.clickAddGiftButton();

        // Инициализируем страницу добавления подарка
        AddGiftPage addGiftPage = new AddGiftPage(driver);

        // Ожидаем появления модального окна
        WaitUtils.waitForVisibility(driver, By.xpath("/html/body/div[3]"));
        addGiftPage.verifyModalDisplayed();

        // Подготавливаем тестовые данные
        String testName = "Test Gift " + System.currentTimeMillis();
        String testDescription = "Описание тестового подарка для автотеста";
        String testProductUrl = "https://flowwow.com/bakery-products/tort-na-den-rozhdeniya-pr-2603/";
        String testPrice = "4300";
        String testImageUrl = "https://content2.flowwow-images.com/data/flowers/1000x1000/35/1730898908_64456035.jpg";

        // Заполняем поля формы
        addGiftPage.getGiftNameField().clear();
        addGiftPage.getGiftNameField().sendKeys(testName);

        addGiftPage.getGiftDescriptionField().clear();
        addGiftPage.getGiftDescriptionField().sendKeys(testDescription);

        addGiftPage.giftUrlProdact().clear();
        addGiftPage.giftUrlProdact().sendKeys(testProductUrl);

        addGiftPage.giftPriceProdact().clear();
        addGiftPage.giftPriceProdact().sendKeys(testPrice);

        addGiftPage.giftUrlImage().clear();
        addGiftPage.giftUrlImage().sendKeys(testImageUrl);

        // Нажимаем кнопку "Добавить"
        addGiftPage.clickSaveButton();

        // Ожидаем исчезновения модального окна
        WaitUtils.waitForInvisibility(driver, By.xpath("/html/body/div[3]"));

        // Переинициализируем страницу вишлиста после добавления подарка
        wishListPage = new MyWishListPage(driver);

        // Ожидаем загрузки обновлённой страницы вишлиста
        WaitUtils.waitForVisibility(driver, By.xpath("//*[@id='root']/div/h2"));

        // Проверяем, что страница вишлиста снова видна
        wishListPage.verifyWishlistPageLoaded();

        // Проверяем изменение количества подарков
        wishListPage.verifyGiftCountIncreased(initialGiftCount);
    }
}