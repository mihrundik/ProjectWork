package tests.WishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.OptionsParser;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenCreateFormTest extends AbstractBaseTest {

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
    @DisplayName("Тест: Открытие формы создания нового вишлиста")
    void testOpenCreateForm() {
        myWishlistsPage.clickAddNewList();
        myWishlistsPage.waitForCreateFormToAppear();

        // переинициализируем элементы модального окна
        myWishlistsPage.initModalElements();

        assertTrue(myWishlistsPage.getNameNewWL().isDisplayed(),
                "Поле названия должно отображаться");
        assertTrue(myWishlistsPage.getDescriptionNewWL().isDisplayed(),
                "Поле описания должно отображаться");

        // закрываем форму чтоб не мешала при след тестах
        myWishlistsPage.clickCloseButton();
        myWishlistsPage.waitForCreateFormToDisappear();

        log.info("Форма создания успешно открылась и закрылась");
    }
}
