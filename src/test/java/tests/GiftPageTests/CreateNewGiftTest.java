package tests.GiftPageTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import pages.MyWishListPage;
import pages.AddGiftPage;
import tests.AbstractBaseTest;
import utils.WaitUtils;
import utils.WishlistHelper;

import static org.junit.jupiter.api.Assertions.*;


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

    @Test
    @DisplayName("Создание нового подарка в вишлисте")
    void createNewGiftTest() {

        // Шаг 1: проверяем наличие списков на уже загруженной странице
        // хелпер для обеспечения наличия вишлиста
        wishlistHelper.ensureWishlistExists();

        // Шаг 2: переходим к последнему вишлисту
        wishlistsPage.clickViewButtonOnLastWishlist();
        MyWishListPage wishListPage = new MyWishListPage(driver);

        // ждём загрузки страницы вишлиста (по заголовку)
        WaitUtils.waitForVisibility(driver, By.xpath("//*[@id='root']/div/h2"));

        // проверяем, что страница вишлиста загрузилась
        assertTrue(wishListPage.isWishlistPageDisplayed(), "Страница вишлиста не загрузилась");

        // Шаг 3: получаем начальное количество подарков
        int initialGiftCount = wishListPage.getGiftItemsCount();
        log.info("Начальное количество подарков: {}", initialGiftCount);

        // Шаг 4: нажимаем кнопку "Добавить подарок"
        wishListPage.clickAddGiftButton();

        // Шаг 5: переинициализируем страницу добавления подарка
        var addGiftPage = new AddGiftPage(driver);

        // ждём появления модального окна
        WaitUtils.waitForVisibility(driver, By.xpath("/html/body/div[3]")); // контейнер модального окна

        // проверяем, что модальное окно отображается
        assertTrue(addGiftPage.isModalDisplayed(), "Модальное окно не отображено");

        // Шаг 6: готовим тестовые данные
        String testName = "Test Gift " + System.currentTimeMillis();
        String testDescription = "Описание тестового подарка для автотеста";
        String testProductUrl = "https://flowwow.com/bakery-products/tort-na-den-rozhdeniya-pr-2603/";
        String testPrice = "4300";
        String testImageUrl = "https://content2.flowwow-images.com/data/flowers/1000x1000/35/1730898908_64456035.jpg";

        // Шаг 7: заполняем поля
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

        // Шаг 8: нажимаем кнопку "Добавить"
        addGiftPage.clickSaveButton();

        // Шаг 9: ждём исчезновения модального окна
        WaitUtils.waitForInvisibility(driver, By.xpath("/html/body/div[3]")); // Контейнер модального окна

        // переинициализируем страницу вишлиста после добавления подарка
        wishListPage = new MyWishListPage(driver);
        int newGiftCount = wishListPage.getGiftItemsCount();

        // ждём загрузки обновлённой страницы вишлиста
        WaitUtils.waitForVisibility(driver, By.xpath("//*[@id='root']/div/h2"));

        // проверяем, что страница вишлиста снова видна
        assertTrue(wishListPage.isWishlistPageDisplayed(), "Страница вишлиста не отображается после добавления подарка");

        // Шаг 11: проверяем изменение количества подарков
        newGiftCount = wishListPage.getGiftItemsCount();
        log.info("Новое количество подарков: {}", newGiftCount);

        // проверяем, что количество подарков увеличилось
        assertEquals(initialGiftCount + 1, newGiftCount,
                "Количество подарков не изменилось после добавления");
    }
}