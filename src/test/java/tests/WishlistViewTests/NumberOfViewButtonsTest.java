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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberOfViewButtonsTest extends AbstractBaseTest {

    private MyWishlistsPage myWishlistsPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        return super.getOptions(browserName);
    }

    @BeforeEach
    void initWishlistPage() {
        // инициализация страницы со списками желаний
        myWishlistsPage = page.myWishlistsPage;

        // дополнительно можно добавить проверку, что страница загружена
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

        // считаем реальные кнопки "Просмотр"
        List<WebElement> viewButtons = driver.findElements(
                By.xpath("//button[contains(text(), 'Просмотр')]")
        );
        int actualViewButtonsCount = viewButtons.size();
        log.info("Реальных кнопок 'Просмотр' на странице: {}", actualViewButtonsCount);

        // они должны совпадать
        assertEquals(actualViewButtonsCount, wishlistCount,
                "Количество вишлистов должно равняться количеству кнопок 'Просмотр'");
    }
}