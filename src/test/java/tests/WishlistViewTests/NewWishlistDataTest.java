package tests.WishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class NewWishlistDataTest extends AbstractBaseTest {

    private MyWishlistsPage wishlistsPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        return super.getOptions(browserName);
    }

    @BeforeEach
    public void setUp() {
        wishlistsPage = new MyWishlistsPage(driver);
        wishlistsPage.open();
    }


    @Test
    @DisplayName("Тест: Проверка данных нового вишлиста")
    void testNewWishlistData() {
        // ВСЕГДА создаем новый список, игнорируя существующие
        log.info("Создаём новый вишлист для теста...");

        wishlistsPage.clickAddNewList();
        wishlistsPage.waitForCreateFormToAppear();

        String tempWishlistName = "Автотест-вишлист " + System.currentTimeMillis();
        String tempWishlistDesc = "Этот вишлист создан для автоматического теста";

        wishlistsPage.fillCreateForm(tempWishlistName, tempWishlistDesc);
        wishlistsPage.clickSubmitButton();
        wishlistsPage.waitForCreateFormToDisappear();

        log.info("Создан временный вишлист: {}", tempWishlistName);

        // Получаем данные последнего (только что созданного) списка
        String lastTitle = wishlistsPage.getLastWishlistTitle();
        String lastDescription = wishlistsPage.getLastWishlistDescription();
        String lastGiftCount = wishlistsPage.getLastWishlistGiftCountText();

        log.info("Созданный вишлист - Название: {}, Описание: {}, Подарки: {}",
                lastTitle, lastDescription, lastGiftCount);

        // Проверяем данные нового списка
        assertNotNull(lastTitle, "Название не должно быть пустым");
        assertFalse(lastTitle.isEmpty(), "Название не должно быть пустой строкой");
        assertEquals(tempWishlistName, lastTitle, "Название не соответствует созданному");
        assertEquals(tempWishlistDesc, lastDescription, "Описание не соответствует созданному");

        // Проверяем количество подарков (должно быть 0)
        assertTrue(lastGiftCount.contains("0"),
                "В новом списке должно быть 0 подарков, получено: " + lastGiftCount);
    }
}
