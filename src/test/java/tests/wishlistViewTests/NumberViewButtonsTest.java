package tests.wishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;


public class NumberViewButtonsTest extends AbstractBaseTest {

    private MyWishlistsPage myWishlistsPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        return super.getOptions(browserName);
    }

    @BeforeEach
    void initWishlistPage() {
        // инициализация страницы со списками желаний
        myWishlistsPage = page.myWishlistsPage;

        // дополнительно что страница загружена
        log.info("Страница MyWishlistsPage инициализирована");
    }

    /**
     * Тест выполняет следующие шаги:
     * 1. Получает количество вишлистов
     * 2. Подсчитывает реальное количество кнопок "Просмотр" на странице
     * 3. Сравнивает полученные значения, они должны совпадать
     */
    @Test
    @DisplayName("Тест: Проверка количества кнопок 'Просмотр'")
    void testNumberOfViewButtons() {
        if (myWishlistsPage == null) {
            throw new IllegalStateException("myWishlistsPage не инициализирована. Проверьте @BeforeEach метод.");
        }

        // Проверяем, что количество вишлистов равно количеству кнопок "Просмотр"
        myWishlistsPage.assertions().verifyNumberOfViewButtonsEqualsWishlistCount();
    }

}