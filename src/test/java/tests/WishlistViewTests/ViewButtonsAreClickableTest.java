package tests.WishlistViewTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ViewButtonsAreClickableTest  extends AbstractBaseTest {

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
    @DisplayName("Тест: Проверка кликабельности кнопок 'Просмотр'")
    void testViewButtonsAreClickable() {
        int wishlistCount = myWishlistsPage.getWishlistCount();
        Assumptions.assumeTrue(wishlistCount > 0, "Тест пропущен: нет списков желаний");

        List<WebElement> wishlistCards = myWishlistsPage.getAllWishlistCards();

        for (int i = 0; i < wishlistCards.size(); i++) {
            WebElement card = wishlistCards.get(i);
            String title = card.findElement(By.xpath(".//div[contains(@class, 'card-title')]")).getText();
            WebElement viewButton = card.findElement(By.xpath(".//button[contains(text(), 'Просмотр')]"));

            assertTrue(viewButton.isEnabled(),
                    String.format("Кнопка 'Просмотр' для вишлиста '%s' должна быть активна", title));

            log.info("Кнопка для вишлиста '{}' активна", title);
        }
    }
}
