package tests.WLDetalPageTests;

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

public class AddGiftButtonTest extends AbstractBaseTest {

    private MyWishlistsPage myWishlistsPage;
    private MyWishListPage myWishListPage;
    private AddGiftPage addGiftPage;

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
    void initPages() {
        // инициализация страниц
        myWishlistsPage = new MyWishlistsPage(driver);
        myWishListPage = new MyWishListPage(driver);
        addGiftPage = new AddGiftPage(driver);
    }

    @Test
    @DisplayName("Тест: Проверка перехода на страницу добавления подарка")
    void testNavigateToAddGiftPage() {
        // пропускаем тест, если нет ни одного списка
        Assumptions.assumeTrue(myWishlistsPage.hasWishlists(),
                "Тест пропущен: нет списков желаний");

        log.info("Начинаем тест проверки кнопки 'Добавить подарок'");

        // получаем информацию о последнем списке до клика
        String lastTitle = myWishlistsPage.getLastWishlistTitle();
        log.info("Выбран список для теста: {}", lastTitle);

        // Шаг 1: нажимаем кнопку "Просмотр" на последнем списке
        myWishlistsPage.clickViewButtonOnLastWishlist();
        log.info("Нажата кнопка 'Просмотр', переходим на страницу деталей списка");

        // Шаг 2: проверяем, что мы на странице деталей списка
        assertTrue(myWishListPage.isWishlistPageDisplayed(),
                "Страница деталей списка должна отображаться");

        String detailPageTitle = myWishListPage.getWishlistTitle();
        assertEquals(lastTitle, detailPageTitle,
                "Заголовок списка на странице деталей должен совпадать");
        log.info("Успешно перешли на страницу деталей списка '{}'", detailPageTitle);

        // Шаг 3: нажимаем кнопку "Добавить подарок"
        myWishListPage.clickAddGiftButton();
        log.info("Нажата кнопка 'Добавить подарок'");

        // Шаг 4: проверяем, что открылась страница добавления подарка
        assertTrue(addGiftPage.isAddGiftPageDisplayed(),
                "Страница добавления подарка должна отображаться");

        // проверяем наличие основных элементов на странице добавления подарка
        assertAll("Проверка элементов страницы добавления подарка",
                () -> assertTrue(addGiftPage.isGiftNameFieldDisplayed(),
                        "Поле названия подарка должно отображаться"),
                () -> assertTrue(addGiftPage.isGiftDescriptionFieldDisplayed(),
                        "Поле описания подарка должно отображаться"),
                () -> assertTrue(addGiftPage.isGiftPriceFieldDisplayed(),
                        "Поле цены подарка должно отображаться"),
                () -> assertTrue(addGiftPage.isGiftUrlFieldDisplayed(),
                        "Поле URL подарка должно отображаться"),
                () -> assertTrue(addGiftPage.isSaveButtonDisplayed(),
                        "Кнопка сохранения должна отображаться"),
                () -> assertTrue(addGiftPage.isCancelButtonDisplayed(),
                        "Кнопка отмены должна отображаться")
        );

        log.info("Все элементы страницы добавления подарка отображаются корректно");

        // проверяем, что страница добавления подарка содержит информацию о текущем списке
        String currentWishlistName = addGiftPage.getCurrentWishlistName();
        assertEquals(lastTitle, currentWishlistName,
                "Название списка на странице добавления должно совпадать");
        log.info("Подтверждена принадлежность к списку '{}'", currentWishlistName);
    }

    @Test
    @DisplayName("Тест: Проверка отмены добавления подарка")
    void testCancelAddGift() {
        // пропускаем тест, если нет ни одного списка
        Assumptions.assumeTrue(myWishlistsPage.hasWishlists(),
                "Тест пропущен: нет списков желаний");

        log.info("Начинаем тест проверки отмены добавления подарка");

        // переходим к странице добавления подарка
        myWishlistsPage.clickViewButtonOnLastWishlist();
        myWishListPage.clickAddGiftButton();

        // проверяем, что мы на странице добавления
        assertTrue(addGiftPage.isAddGiftPageDisplayed(),
                "Страница добавления подарка должна отображаться");

        // нажимаем кнопку отмены
        addGiftPage.clickCancelButton();
        log.info("Нажата кнопка отмены");

        // проверяем, что вернулись на страницу деталей списка
        assertTrue(myWishListPage.isWishlistPageDisplayed(),
                "После отмены должны вернуться на страницу деталей списка");

        log.info("Успешно вернулись на страницу деталей списка после отмены");
    }

    @Test
    @DisplayName("Тест: Проверка создания нового подарка")
    void testCreateNewGift() {
        // пропускаем тест, если нет ни одного списка
        Assumptions.assumeTrue(myWishlistsPage.hasWishlists(),
                "Тест пропущен: нет списков желаний");

        log.info("Начинаем тест создания нового подарка");

        // запоминаем текущее количество подарков в списке
        int initialGiftCount = myWishlistsPage.getLastWishlistGiftCount();
        log.info("Начальное количество подарков: {}", initialGiftCount);

        // переходим к странице добавления подарка
        myWishlistsPage.clickViewButtonOnLastWishlist();
        myWishListPage.clickAddGiftButton();

        // данные для нового подарка
        String giftName = "Тестовый подарок " + System.currentTimeMillis();
        String giftDescription = "Описание тестового подарка";
        String giftPrice = "1000";
        String giftUrl = "https://example.com/gift";

        // заполняем форму
        addGiftPage.enterGiftName(giftName);
        addGiftPage.enterGiftDescription(giftDescription);
        addGiftPage.enterGiftPrice(giftPrice);
        addGiftPage.enterGiftUrl(giftUrl);

        log.info("Заполнены данные нового подарка: {}", giftName);

        // сохраняем подарок
        addGiftPage.clickSaveButton();
        log.info("Нажата кнопка сохранения");

        // проверяем, что вернулись на страницу деталей списка
        assertTrue(myWishListPage.isWishlistPageDisplayed(),
                "После сохранения должны вернуться на страницу деталей списка");

        // проверяем, что подарок появился в списке
        assertTrue(myWishListPage.hasGifts(),
                "После добавления подарка список не должен быть пустым");

        int newGiftCount = myWishListPage.getGiftItemsCount();
        assertEquals(initialGiftCount + 1, newGiftCount,
                "Количество подарков должно увеличиться на 1");

        log.info("Подарок '{}' успешно создан. Количество подарков увеличилось с {} до {}",
                giftName, initialGiftCount, newGiftCount);
    }
}