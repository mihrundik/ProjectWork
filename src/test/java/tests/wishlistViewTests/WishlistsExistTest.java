package tests.wishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WishlistHelper;


public class WishlistsExistTest extends AbstractBaseTest {

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
     * 2. Проверяет, что вишлисты присутствуют на странице
     */
    @Test
    @DisplayName("Тест: Проверка наличия списков желаний")
    void testWishlistsExist() {
        // Обеспечиваем наличие вишлиста
        wishlistHelper.ensureWishlistExists();

        // Проверяем наличие вишлистов на странице
        wishlistsPage.verifyWishlistsExist();

        log.info("Тест пройден успешно.");
    }

}