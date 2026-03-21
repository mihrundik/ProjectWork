package tests.WishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import factory.sattings.OptionsParser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberOfViewButtonsTest extends AbstractBaseTest {

    private MyWishlistsPage myWishlistsPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        // проверяем опции в командной строке
        String optionsFromCmd = null;
        switch (browserName.toLowerCase()) {
            case "chrome":
                optionsFromCmd = System.getProperty("chromeOptions");
                break;
            case "firefox":
                optionsFromCmd = System.getProperty("firefoxOptions");
                break;
            case "safari":
                optionsFromCmd = System.getProperty("safariOptions");
                break;
            case "edge":
                optionsFromCmd = System.getProperty("edgeOptions");
                break;
        }

        // если есть - парсим их
        if (optionsFromCmd != null && !optionsFromCmd.isEmpty()) {
            return OptionsParser.parse(browserName, optionsFromCmd);
        }

        // или используем стандартные опции
        switch (browserName.toLowerCase()) {
            case "chrome":
                return new ChromeOptions();
            case "firefox":
                return new FirefoxOptions();
            case "safari":
                return new SafariOptions();
            case "edge":
                return new EdgeOptions();
            default:
                throw new IllegalArgumentException("Неподдерживаемый браузер: " + browserName);
        }
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
        // Проверяем, что страница инициализирована
        if (myWishlistsPage == null) {
            throw new IllegalStateException("myWishlistsPage не инициализирована. Проверьте @BeforeEach метод.");
        }

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