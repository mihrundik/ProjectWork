package tests.wishlistViewTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WishlistHelper;


public class ViewButtonsClickableTest extends AbstractBaseTest {

    private MyWishlistsPage myWishlistsPage;
    private WishlistHelper wishlistHelper;

    @Override
    protected Capabilities getOptions(String browserName) {
        return super.getOptions(browserName);
    }

    @BeforeEach
    void initWishlistPage() {
        // Инициализируем объекты страниц и хелперы
        myWishlistsPage = new MyWishlistsPage(driver);
        wishlistHelper = new WishlistHelper(driver);

        // Открываем страницу "Мои списки"
        myWishlistsPage.open();
    }

    /**
     * Тест выполняет следующие шаги:
     * 1. Проверяет наличие вишлистов, при отсутствии создает новый
     * 2. Получает все карточки вишлистов
     * 3. Для каждого вишлиста проверяет, что кнопка "Просмотр" активна
     */
    @Test
    @DisplayName("Тест: Проверка кликабельности кнопок 'Просмотр'")
    void testViewButtonsAreClickable() {

        // Проверяем наличие вишлистов, при отсутствии создаем новый
        wishlistHelper.ensureWishlistExists();

        // Проверяем наличие списков, иначе пропускаем тест
        int wishlistCount = myWishlistsPage.getWishlistCount();
        Assumptions.assumeTrue(wishlistCount > 0, "Тест пропущен: нет списков желаний");

        // Проверяем кликабельность кнопки "Просмотр" для каждого вишлиста
        myWishlistsPage.assertions().verifyAllViewButtonsClickable();
    }

}
