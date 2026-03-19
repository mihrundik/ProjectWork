package tests.GiftPageTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;
import pages.MyWishlistsPage;
import pages.MyWishListPage;
import pages.AddGiftPage;
import tests.AbstractBaseTest;
import utils.OptionsParser;

import static org.junit.jupiter.api.Assertions.*;

public class CreateNewGiftTest extends AbstractBaseTest {

    private MyWishlistsPage wishlistsPage;
    private MyWishListPage wishListPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        // проверяем опции в командной строке
        String optionsFromCmd = null;
        switch (browserName.toLowerCase()) {
            case "chrome":
                optionsFromCmd = System.getProperty("chromeOptions");
                break;
            case "firefox":
                optionsFromCmd = System.getProperty("firefoxOptions");
                break;
            case "safari":
                optionsFromCmd = System.getProperty("safariOptions");
                break;
            case "edge":
                optionsFromCmd = System.getProperty("edgeOptions");
                break;
        }

        // если есть - парсим их
        if (optionsFromCmd != null && !optionsFromCmd.isEmpty()) {
            return OptionsParser.parse(browserName, optionsFromCmd);
        }

        // или используем стандартные опции
        switch (browserName.toLowerCase()) {
            case "chrome":
                return new ChromeOptions();
            case "firefox":
                return new FirefoxOptions();
            case "safari":
                return new SafariOptions();
            case "edge":
                return new EdgeOptions();
            default:
                throw new IllegalArgumentException("Неподдерживаемый браузер: " + browserName);
        }
    }

    @BeforeEach
    public void setUp() {
        wishlistsPage = new MyWishlistsPage(driver);
        wishlistsPage.open();
    }


    @Test
    @DisplayName("Тест: Проверка кнопки Добавить в окне создания нового подарка")
    void testCreateNewGift() {
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
        wishListPage = new MyWishListPage(driver);

        // проверяем, что страница вишлиста загрузилась
        assertTrue(wishListPage.isWishlistPageDisplayed(), "Страница вишлиста не загрузилась");

        // ШАГ 1: Запоминаем изначальное количество подарков в списке
        int initialGiftCount = wishListPage.getGiftItemsCount();
        log.info("Изначальное количество подарков в списке: {}", initialGiftCount);

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

        addGiftPage.giftPriceProdact().clear();
        addGiftPage.giftPriceProdact().sendKeys(testPrice);

        addGiftPage.giftUrlImage().clear();
        addGiftPage.giftUrlImage().sendKeys(testImageUrl);

        // ШАГ 2: Кликаем кнопку "Добавить" (а не крестик)
        addGiftPage.clickSaveButton();

        // переинициализируем элементы страницы, так как старый объект может содержать устаревшие данные
        wishlistsPage = new MyWishlistsPage(driver);

        // убедится, что страница вишлиста снова видима
        assertTrue(wishListPage.isWishlistPageDisplayed(), "После закрытия модалки страница вишлиста не отображается");

        // ШАГ 3: Проверяем, что количество подарков увеличилось на 1
        int newGiftCount = wishListPage.getGiftItemsCount();
        log.info("Новое количество подарков в списке: {}", newGiftCount);

        assertEquals(initialGiftCount + 1, newGiftCount,
                "Количество подарков не увеличилось на 1 после добавления");
    }
}