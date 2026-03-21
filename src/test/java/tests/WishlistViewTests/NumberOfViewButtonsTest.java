package tests.WishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberOfViewButtonsTest extends AbstractBaseTest {

    private MyWishlistsPage myWishlistsPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        return getBrowserOptions(browserName);
    }

    @BeforeEach
    void initWishlistPage() {
        // Инициализация страницы со списками желаний
        myWishlistsPage = page.myWishlistsPage;

        // Дополнительно можно добавить проверку, что страница загружена
        log.info("Страница MyWishlistsPage инициализирована");
    }

    @Test
    @DisplayName("Тест: Проверка количества кнопок 'Просмотр'")
    void testNumberOfViewButtons() {
        // проверяем, что страница инициализирована
        if (myWishlistsPage == null) {
            throw new IllegalStateException("myWishlistsPage не инициализирована. Проверьте @BeforeEach метод.");
        }

        int wishlistCount = myWishlistsPage.getWishlistCount();
        log.info("getWishlistCount() вернул: {}", wishlistCount);

        // находим все кнопки "Просмотр"
        List<WebElement> allViewButtons = driver.findElements(
                By.xpath("//button[contains(text(), 'Просмотр')]")
        );

        // фильтруем только видимые и enabled кнопки
        List<WebElement> visibleButtons = allViewButtons.stream()
                .filter(WebElement::isDisplayed)
                .filter(WebElement::isEnabled)
                .collect(Collectors.toList());

        // ищем уникальные кнопки по их местоположению
        List<WebElement> uniqueButtons = visibleButtons.stream()
                .distinct()
                .collect(Collectors.toList());

        int actualViewButtonsCount = uniqueButtons.size();
        log.info("Реальных уникальных видимых кнопок 'Просмотр' на странице: {}", actualViewButtonsCount);

        assertEquals(wishlistCount, actualViewButtonsCount,
                String.format("Количество вишлистов (%d) должно равняться количеству уникальных кнопок 'Просмотр' (%d)",
                        wishlistCount, actualViewButtonsCount));
    }

}