package tests.WishlistViewTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WishlistHelper;


public class ClickViewButtonOnLastWishlistTest extends AbstractBaseTest {

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
    @DisplayName("Тест: Клик по кнопке 'Просмотр' последнего списка")
    void testClickViewButtonOnLastWishlist() {

        // Шаг 1: проверяем наличие списков на уже загруженной странице
        // хелпер для обеспечения наличия вишлиста
        wishlistHelper.ensureWishlistExists();

        Assumptions.assumeTrue(wishlistsPage.hasWishlists(),
                "Тест пропущен: нет списков желаний");

        String lastTitle = wishlistsPage.getLastWishlistTitle();
        String lastDescription = wishlistsPage.getLastWishlistDescription();
        String lastGiftCount = wishlistsPage.getLastWishlistGiftCountText();

        log.info("До клика: Заголовок: {}, Описание: {}, Подарки: {}",
                lastTitle, lastDescription, lastGiftCount);

        wishlistsPage.clickViewButtonOnLastWishlist();

        // если мы дошли до сюда без исключений - значит клик прошел успешно
        log.info("Клик по кнопке 'Просмотр' успешно выполнен");
    }
}
