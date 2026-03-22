package tests.WishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WishlistsExistTest extends AbstractBaseTest {

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
    @DisplayName("Тест: Проверка наличия списков желаний")
    void testWishlistsExist() {
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

        // Шаг 2: проверка наличия вишлистов
        assertTrue(wishlistsPage.hasWishlists(),
                "Должен быть хотя бы один список желаний");

        log.info("Тест пройден успешно.");
    }
}