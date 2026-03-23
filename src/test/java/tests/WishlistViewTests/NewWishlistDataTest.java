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
        // Шаг 1: проверяем наличие списков на уже загруженной странице
        if (!wishlistsPage.hasWishlists()) {
            log.info("Списки желаний отсутствуют. Создаём новый...");

            // Шаг 1.1: вызываем метод на существующем объекте страницы.
            wishlistsPage.clickAddNewList();

            // ждём появления формы (используем улучшенный метод)
            wishlistsPage.waitForCreateFormToAppear(); // Теперь это работает корректно!

            String tempWishlistName = "Автотест-вишлист " + System.currentTimeMillis();
            String tempWishlistDesc = "Этот вишлист создан для автоматического теста";

            wishlistsPage.fillCreateForm(tempWishlistName, tempWishlistDesc);
            wishlistsPage.clickSubmitButton();

            // ждём исчезновение формы
            wishlistsPage.waitForCreateFormToDisappear();
            log.info("Создан временный вишлист: {}", tempWishlistName);
        }
        // убеждаемся, что есть хотя бы один список
        assertTrue(wishlistsPage.hasWishlists(),
                "Должен быть хотя бы один список желаний");

        // получаем данные последнего (самого нового?) списка
        String lastTitle = wishlistsPage.getLastWishlistTitle();
        String lastDescription = wishlistsPage.getLastWishlistDescription();
        String lastGiftCount = wishlistsPage.getLastWishlistGiftCountText();

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
