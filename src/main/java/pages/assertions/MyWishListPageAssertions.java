package pages.assertions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.MyWishListPage;

import static org.junit.jupiter.api.Assertions.*;

public class MyWishListPageAssertions {

    private final MyWishListPage page;
    public Logger log = LogManager.getLogger(MyWishListPageAssertions.class);

    public MyWishListPageAssertions(MyWishListPage page) {
        this.page = page;
    }

    /**
     * Проверяет, что страница вишлиста загружена
     */
    public void verifyWishlistPageLoaded() {
        assertTrue(page.isWishlistPageDisplayed(), "Страница вишлиста не загрузилась");
    }

    /**
     * Проверяет, что страница вишлиста отображается после закрытия модального окна
     */
    public void verifyWishlistPageDisplayedAfterModalClosed() {
        assertTrue(page.isWishlistPageDisplayed(), "После закрытия модального окна страница вишлиста не отображается");
    }

    /**
     * Проверяет, что количество подарков увеличилось на 1
     */
    public void verifyGiftCountIncreased(int initialCount) {
        int newCount = page.getGiftItemsCount();
        assertEquals(initialCount + 1, newCount,
                "Количество подарков должно увеличиться на 1. Было: " + initialCount + ", стало: " + newCount);
    }

    /**
     * Проверяет, что модальное окно добавления подарка открыто с правильным заголовком
     */
    public void verifyAddGiftModalOpened() {
        assertTrue(page.isAddGiftModalDisplayedWithTitle("Добавить подарок"),
                "Модальное окно 'Добавить подарок' не появилось или заголовок не совпадает");
    }

    /**
     * Проверяет соответствие названия и описания вишлиста
     */
    public void verifyWishlistData(String expectedTitle, String expectedDescription) {
        String actualTitle = page.getWishlistTitle();
        String actualDescription = page.getWishlistDescription();

        assertEquals(expectedTitle, actualTitle,
                String.format("Название списка не совпадает. Ожидалось: '%s', Фактически: '%s'",
                        expectedTitle, actualTitle));

        assertEquals(expectedDescription, actualDescription,
                String.format("Описание списка не совпадает. Ожидалось: '%s', Фактически: '%s'",
                        expectedDescription, actualDescription));

        log.info("Проверка данных вишлиста успешно выполнена");
    }

    /**
     * Проверяет, что кнопка "Добавить подарок" кликабельна
     */
    public void verifyAddGiftButtonClickable() {
        page.wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(page.getAddGiftButton()));
        // Если дошли до этой строки, значит элемент кликабельный
        log.info("Кнопка 'Добавить подарок' кликабельна");
    }

    /**
     * Проверяет, что кнопка "Удалить список" кликабельна
     */
    public void verifyDeleteWishlistButtonClickable() {
        page.wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(page.getDeleteWishlistButton()));
        // Если дошли до этой строки, значит элемент кликабельный
        log.info("Кнопка 'Удалить список' кликабельна");
    }
}