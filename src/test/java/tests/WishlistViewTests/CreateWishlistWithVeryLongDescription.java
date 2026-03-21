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
import factory.sattings.OptionsParser;

import static org.junit.jupiter.api.Assertions.*;

public class CreateWishlistWithVeryLongDescription extends AbstractBaseTest {

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
    @DisplayName("Тест: Попытка создания вишлиста с очень длинным описанием (512 символов) - форма не закрывается")
    void testCreateWishlistWithVeryLongDescription() {
        String testListName = "Тестовый список " + System.currentTimeMillis();
        String testListDescription = "B".repeat(512);

        log.info("Длина описания: {} символов", testListDescription.length());

        int initialCount = myWishlistsPage.getWishlistCount();
        log.info("Начальное количество списков: {}", initialCount);

        myWishlistsPage.clickAddNewList();
        myWishlistsPage.waitForCreateFormToAppear();

        myWishlistsPage.fillCreateForm(testListName, testListDescription);
        myWishlistsPage.clickSubmitButton();

        // проверяем, что форма НЕ закрылась
        boolean formStillVisible = myWishlistsPage.isCreateFormVisible();
        log.info("Форма все еще видна: {}", formStillVisible);

        assertTrue(formStillVisible,
                "При очень длинном описании форма должна оставаться открытой");

        // проверяем, что количество списков не изменилось
        int currentCount = myWishlistsPage.getWishlistCount();
        log.info("Количество списков после попытки создания: {}", currentCount);

        assertEquals(initialCount, currentCount,
                "Количество списков не должно измениться при очень длинном описании");

        // закрываем форму, чтобы не влиять на другие тесты
        myWishlistsPage.closeCreateForm();
        log.info("Форма закрыта");
    }
}