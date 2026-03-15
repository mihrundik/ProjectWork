package tests.WishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NewWishlistDataTest extends AbstractBaseTest {

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
    @DisplayName("Тест: Проверка данных нового вишлиста")
    void testNewWishlistData() {
        // убеждаемся, что есть хотя бы один список
        assertTrue(myWishlistsPage.hasWishlists(),
                "Должен быть хотя бы один список желаний");

        // получаем данные последнего (самого нового?) списка
        String lastTitle = myWishlistsPage.getLastWishlistTitle();
        String lastDescription = myWishlistsPage.getLastWishlistDescription();
        String lastGiftCount = myWishlistsPage.getLastWishlistGiftCountText();

        log.info("Последний вишлист - Название: {}, Описание: {}, Подарки: {}",
                lastTitle, lastDescription, lastGiftCount);

        // проверяем, что у последнего списка есть данные
        assertNotNull(lastTitle, "Название не должно быть пустым");
        assertFalse(lastTitle.isEmpty(), "Название не должно быть пустой строкой");

        // проверяем количество подарков
        assertTrue(lastGiftCount.contains("0"),
                "В новом списке должно быть 0 подарков, получено: " + lastGiftCount);
    }
}
