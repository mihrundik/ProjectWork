package tests.WishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenCreateFormTest extends AbstractBaseTest {

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
