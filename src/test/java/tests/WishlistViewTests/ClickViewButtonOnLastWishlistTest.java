package tests.WishlistViewTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

public class ClickViewButtonOnLastWishlistTest extends AbstractBaseTest {

    private MyWishlistsPage myWishlistsPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        return getBrowserOptions(browserName);
    }

    @BeforeEach
    void initWishlistPage() {
        // инициализация определенной страницы
        myWishlistsPage = page.myWishlistsPage;
    }

    @Test
    @DisplayName("Тест: Клик по кнопке 'Просмотр' последнего списка")
    void testClickViewButtonOnLastWishlist() {
        // убеждаемся, что есть хотя бы один список
        Assumptions.assumeTrue(myWishlistsPage.hasWishlists(),
                "Тест пропущен: нет списков желаний");

        String lastTitle = myWishlistsPage.getLastWishlistTitle();
        String lastDescription = myWishlistsPage.getLastWishlistDescription();
        String lastGiftCount = myWishlistsPage.getLastWishlistGiftCountText();

        log.info("До клика: Заголовок: {}, Описание: {}, Подарки: {}",
                lastTitle, lastDescription, lastGiftCount);

        myWishlistsPage.clickViewButtonOnLastWishlist();

        // если мы дошли до сюда без исключений - значит клик прошел успешно
        log.info("Клик по кнопке 'Просмотр' успешно выполнен");
    }
}