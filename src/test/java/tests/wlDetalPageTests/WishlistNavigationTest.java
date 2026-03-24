package tests.wlDetalPageTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishListPage;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WishlistHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class WishlistNavigationTest extends AbstractBaseTest {

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
     * 2. Сохраняет название и описание последнего вишлиста из списка
     * 3. Переходит на страницу этого вишлиста по кнопке "Просмотр"
     * 4. Получает название и описание на странице вишлиста
     * 5. Сравнивает данные со страницы списка и страницы вишлиста
     */
    @Test
    @DisplayName("Проверка соответствия названия и описания списка подарков при переходе по кнопке Просмотр")
    public void verifyWishlistTitleAfterNavigation() {

        // Обеспечиваем наличие вишлиста
        wishlistHelper.ensureWishlistExists();

        // Получаем название и описание последнего списка на странице списков
        String expectedTitle = wishlistsPage.getLastWishlistTitle();
        String expectedDescription = wishlistsPage.getLastWishlistDescription();

        log.info("Название списка на странице 'Мои списки': {}", expectedTitle);
        log.info("Описание списка на странице 'Мои списки': {}", expectedDescription);

        // Нажимаем кнопку "Просмотр" для последнего списка
        wishlistsPage.clickViewButtonOnLastWishlist();

        // Инициализируем страницу конкретного вишлиста
        MyWishListPage wishListPage = new MyWishListPage(driver);

        // Проверяем, что страница вишлиста загрузилась
        assertTrue(wishListPage.isWishlistPageDisplayed(),
                "Страница вишлиста не загрузилась");

        // Получаем фактическое название и описание на странице вишлиста
        String actualTitle = wishListPage.getWishlistTitle();
        String actualDescription = wishListPage.getWishlistDescription();

        log.info("Название списка на странице вишлиста: {}", actualTitle);
        log.info("Описание списка на странице вишлиста: {}", actualDescription);

        // Сравниваем названия
        assertEquals(actualTitle, expectedTitle,
                String.format("Название списка не совпадает. Ожидалось: '%s', Фактически: '%s'",
                        expectedTitle, actualTitle));

        // Сравниваем описания
        assertEquals(actualDescription, expectedDescription,
                String.format("Описание списка не совпадает. Ожидалось: '%s', Фактически: '%s'",
                        expectedDescription, actualDescription));
    }

}