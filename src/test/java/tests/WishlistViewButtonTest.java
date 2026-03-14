package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.MyWishlistsPage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WishlistViewButtonTest extends AbstractBaseTest {

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
    @Order(1)
    @DisplayName("Тест: Проверка наличия списков желаний")
    void testWishlistsExist() {
        assertTrue(myWishlistsPage.hasWishlists(),
                "Должен быть хотя бы один список желаний для тестирования кнопки 'Просмотр'");

        log.info("Количество списков: {}", myWishlistsPage.getWishlistCount());
        log.info("Заголовок последнего списка: {}", myWishlistsPage.getLastWishlistTitle());
    }

    @Test
    @Order(2)
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

    @Test
    @Order(3)
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

    @Test
    @Order(4)
    @DisplayName("Тест: Клик по кнопке 'Просмотр' последнего списка")
    void testClickViewButtonOnLastWishlist() {
        Assumptions.assumeTrue(myWishlistsPage.hasWishlists(),
                "Тест пропущен: нет списков желаний");

        String lastTitle = myWishlistsPage.getLastWishlistTitle();
        String lastDescription = myWishlistsPage.getLastWishlistDescription();
        String lastGiftCount = myWishlistsPage.getLastWishlistGiftCountText();

        log.info("До клика: Заголовок: {}, Описание: {}, Подарки: {}",
                lastTitle, lastDescription, lastGiftCount);

        myWishlistsPage.clickViewButtonOnLastWishlist();

        // если мы дошли до сюда без исключений - значит клик прошел успешно
        log.info("Клик по кнопке 'Просмотр' успешно выполнен");
    }

}