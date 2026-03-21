package tests.WishlistViewTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import factory.sattings.OptionsParser;

public class ClickViewButtonOnLastWishlistTest extends AbstractBaseTest {

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
        // инициализация определенной страницы
        myWishlistsPage = page.myWishlistsPage;
    }

    @Test
    @DisplayName("Тест: Клик по кнопке 'Просмотр' последнего списка")
    void testClickViewButtonOnLastWishlist() {
        // убеждаемся, что есть хотя бы один список
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