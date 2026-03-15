package tests.WishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberOfViewButtonsTest extends AbstractBaseTest {

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
        @DisplayName("Тест: Проверка количества кнопок 'Просмотр'")
        void testNumberOfViewButtons() {
            int wishlistCount = myWishlistsPage.getWishlistCount();
            log.info("getWishlistCount() вернул: {}", wishlistCount);

            // Считаем реальные кнопки "Просмотр"
            List<WebElement> viewButtons = driver.findElements(
                    By.xpath("//button[contains(text(), 'Просмотр')]")
            );
            int actualViewButtonsCount = viewButtons.size();
            log.info("Реальных кнопок 'Просмотр' на странице: {}", actualViewButtonsCount);

            // Теперь они должны совпадать
            assertEquals(actualViewButtonsCount, wishlistCount,
                    "Количество вишлистов должно равняться количеству кнопок 'Просмотр'");
        }
}
