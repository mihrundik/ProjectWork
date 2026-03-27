package pages.assertions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import pages.MyWishlistsPage;
import utils.WaitUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MyWishlistsPageAssertions {

    private final MyWishlistsPage page;
    public Logger log = LogManager.getLogger(MyWishlistsPageAssertions.class);

    public MyWishlistsPageAssertions(MyWishlistsPage page) {
        this.page = page;
    }

    /**
     * Проверяет, что вишлисты существуют
     */
    public void verifyWishlistsExist() {
        assertTrue(page.hasWishlists(), "Должен быть хотя бы один список желаний");
    }

    /**
     * Проверяет, что кнопка просмотра активна для всех вишлистов
     */
    public void verifyAllViewButtonsClickable() {
        List<WebElement> wishlistCards = page.getAllWishlistCards();
        for (WebElement card : wishlistCards) {
            String title = card.findElement(org.openqa.selenium.By.xpath(".//div[contains(@class, 'card-title')]")).getText();
            WebElement viewButton = card.findElement(org.openqa.selenium.By.xpath(".//button[contains(text(), 'Просмотр')]"));

            assertTrue(viewButton.isEnabled(),
                    String.format("Кнопка 'Просмотр' для вишлиста '%s' должна быть активна", title));
        }
    }

    /**
     * Проверяет, что вишлист был удален
     */
    public void verifyWishlistDeleted(int initialCount) {
        page.waitForPageToLoad();
        int newCount = page.getWishlistCount();
        assertEquals(initialCount - 1, newCount,
                String.format("Количество вишлистов должно уменьшиться на 1. Было: %d, стало: %d", initialCount, newCount));
    }

    /**
     * Проверяет изменение количества вишлистов
     */
    public void verifyWishlistCountChanged(int expectedCount, String operation) {
        int actualCount = page.getWishlistCount();
        assertEquals(expectedCount, actualCount,
                String.format("Количество списков %s: ожидалось %d, фактически %d",
                        operation, expectedCount, actualCount));
        log.info("Количество списков {}: {} (ожидалось {})", operation, actualCount, expectedCount);
    }

    /**
     * Проверяет, что поля формы создания отображаются
     */
    public void verifyCreateFormFieldsDisplayed() {
        page.initModalElements();
        WebElement nameField = page.getNameNewWL();
        WebElement descField = page.getDescriptionNewWL();
        assertTrue(nameField != null && nameField.isDisplayed(), "Поле названия должно отображаться");
        assertTrue(descField != null && descField.isDisplayed(), "Поле описания должно отображаться");
    }

    /**
     * Проверяет, что количество кнопок "Просмотр" равно количеству вишлистов
     */
    public void verifyNumberOfViewButtonsEqualsWishlistCount() {
        int wishlistCount = page.getWishlistCount();
        List<WebElement> viewButtons = page.driver.findElements(org.openqa.selenium.By.xpath("//button[contains(text(), 'Просмотр')]"));
        int actualViewButtonsCount = viewButtons.size();

        assertEquals(wishlistCount, actualViewButtonsCount,
                "Количество вишлистов должно равняться количеству кнопок 'Просмотр'");
    }

    /**
     * Проверяет данные нового вишлиста
     */
    public void verifyNewWishlistData(String expectedTitle, String expectedDescription, String expectedGiftCount) {
        String actualTitle = page.getLastWishlistTitle();
        String actualDescription = page.getLastWishlistDescription();
        String actualGiftCount = page.getLastWishlistGiftCountText();

        assertTrue(actualTitle != null, "Название не должно быть пустым");
        assertTrue(!actualTitle.isEmpty(), "Название не должно быть пустой строкой");
        assertEquals(expectedTitle, actualTitle, "Название не соответствует созданному");
        assertEquals(expectedDescription, actualDescription, "Описание не соответствует созданному");

        assertTrue(actualGiftCount.contains(expectedGiftCount),
                "В новом списке должно быть " + expectedGiftCount + " подарков, получено: " + actualGiftCount);
    }

    /**
     * Проверяет, что форма создания вишлиста не видима
     */
    public void verifyCreateFormNotVisible() {
        // Ожидаем исчезновения формы с использованием WaitUtils
        boolean isInvisible = WaitUtils.waitForInvisibility(page.driver,
                org.openqa.selenium.By.cssSelector("input.form-control[type='text'][required]"));

        // Дополнительно проверяем, что форма действительно не видима
        boolean isVisible = page.isCreateFormVisible();
        assertTrue(!isVisible, "Форма создания не должна быть видима. Текущее состояние: " + isVisible);
        log.info("Форма создания вишлиста не видима");
    }

    /**
     * Проверяет, что клик по кнопке "Просмотр" последнего вишлиста выполнен успешно
     */
    public void verifyViewButtonClickable() {
        page.clickViewButtonOnLastWishlist();
        // Если дошли до этого места без исключений - клик выполнен успешно
        log.info("Клик по кнопке 'Просмотр' успешно выполнен");
    }

    /**
     * Проверяет, что форма создания вишлиста видима
     */
    public void verifyCreateFormVisible() {
        // Ожидаем появления формы с использованием WaitUtils
        try {
            WaitUtils.waitForVisibility(page.driver,
                    org.openqa.selenium.By.cssSelector("input.form-control[type='text'][required]"));
            boolean isVisible = page.isCreateFormVisible();
            assertTrue(isVisible, "Форма создания должна быть видима");
            log.info("Форма создания вишлиста видима");
        } catch (Exception e) {
            log.error("Форма создания не появилась: {}", e.getMessage());
            assertTrue(false, "Форма создания не появилась");
        }
    }
}