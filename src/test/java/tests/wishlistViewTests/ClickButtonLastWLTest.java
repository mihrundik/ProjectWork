package tests.wishlistViewTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WishlistHelper;


public class ClickButtonLastWLTest extends AbstractBaseTest {

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
     * 3. Выполняет клик по кнопке "Просмотр"
     * 4. Проверяет, что клик выполнен успешно
     */
    @Test
    @DisplayName("Тест: Клик по кнопке 'Просмотр' последнего списка")
    void testClickViewButtonOnLastWishlist() {

        // Обеспечиваем наличие вишлиста
        wishlistHelper.ensureWishlistExists();

        // Проверяем наличие списков
        Assumptions.assumeTrue(wishlistsPage.hasWishlists(),
                "Тест пропущен: нет списков желаний");

        // Выполняем клик по кнопке "Просмотр" и проверяем успешность
        wishlistsPage.assertions().verifyViewButtonClickable();

        log.info("Клик по кнопке 'Просмотр' успешно выполнен");
    }

}
