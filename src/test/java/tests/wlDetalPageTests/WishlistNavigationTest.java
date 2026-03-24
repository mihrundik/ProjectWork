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


    @Test
    @DisplayName("Проверка соответствия названия и описания списка подарков при переходе по кнопке Просмотр")
    public void verifyWishlistTitleAfterNavigation() {

        // Шаг 1: проверяем наличие списков на уже загруженной странице
        // хелпер для обеспечения наличия вишлиста
        wishlistHelper.ensureWishlistExists();

        // Шаг 1: получаем название последнего списка на странице списков по которому будем кликать
        String expectedTitle = wishlistsPage.getLastWishlistTitle();
        String expectedDescription = wishlistsPage.getLastWishlistDescription();

        log.info("Название списка на странице 'Мои списки': {}", expectedTitle);
        log.info("Описание списка на странице 'Мои списки': {}", expectedDescription);

        // Шаг 2: нажимаем кнопку "Просмотр" для последнего списка
        wishlistsPage.clickViewButtonOnLastWishlist();

        // Шаг 3: инициализируем страницу конкретного вишлиста
        MyWishListPage wishListPage = new MyWishListPage(driver);

        // Шаг 4: проверяем, что страница вишлиста загрузилась
        assertTrue(wishListPage.isWishlistPageDisplayed(),
                "Страница вишлиста не загрузилась");

        // Шаг 5: получаем фактическое название на странице вишлиста
        String actualTitle = wishListPage.getWishlistTitle();
        String actualDescription = wishListPage.getWishlistDescription();

        log.info("Название списка на странице вишлиста: {}", actualTitle);
        log.info("Описание списка на странице вишлиста: {}", actualDescription);

        // Шаг 6: сравниваем названия
        assertEquals(actualTitle, expectedTitle,
                String.format("Название списка не совпадает. Ожидалось: '%s', Фактически: '%s'",
                        expectedTitle, actualTitle));

        // Шаг 7: сравниваем описания (опционально)
        assertEquals(actualDescription, expectedDescription,
                String.format("Описание списка не совпадает. Ожидалось: '%s', Фактически: '%s'",
                        expectedDescription, actualDescription));


    }

}