package tests.WishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WishlistHelper;

import static org.junit.jupiter.api.Assertions.assertTrue;


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


    @Test
    @DisplayName("Тест: Проверка наличия списков желаний")
    void testWishlistsExist() {
        // Шаг 1: проверяем наличие списков на уже загруженной странице
        // хелпер для обеспечения наличия вишлиста
        wishlistHelper.ensureWishlistExists();

        // Шаг 2: проверка наличия вишлистов
        assertTrue(wishlistsPage.hasWishlists(),
                "Должен быть хотя бы один список желаний");

        log.info("Тест пройден успешно.");
    }
}