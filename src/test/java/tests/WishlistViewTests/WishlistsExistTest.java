package tests.WishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WishlistsExistTest extends AbstractBaseTest {

    private MyWishlistsPage myWishlistsPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        switch (browserName.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                return chromeOptions;
            default:
                return null;
        }
    }

    @BeforeEach
    void initWishlistPage() {
        // инициализация определенной страницы
        myWishlistsPage = page.myWishlistsPage;
    }


    @Test
    @DisplayName("Тест: Проверка наличия списков желаний")
    void testWishlistsExist() {
        assertTrue(myWishlistsPage.hasWishlists(),
                "Должен быть хотя бы один список желаний для тестирования кнопки 'Просмотр'");

        log.info("Количество списков: {}", myWishlistsPage.getWishlistCount());
        log.info("Заголовок последнего списка: {}", myWishlistsPage.getLastWishlistTitle());
    }

}
