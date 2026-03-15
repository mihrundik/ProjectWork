package tests.WishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CloseFormTest extends AbstractBaseTest {

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
    @DisplayName("Тест: Закрытие формы через крестик")
    void testCloseForm() {
        String testListName = "Тестовый список " + System.currentTimeMillis();
        String testListDescription = "Описание тестового списка для закрытия";

        int initialCount = myWishlistsPage.getWishlistCount();
        log.info("Начальное количество списков: {}", initialCount);

        myWishlistsPage.clickAddNewList();
        myWishlistsPage.waitForCreateFormToAppear();

        myWishlistsPage.fillCreateForm(testListName, testListDescription);
        myWishlistsPage.clickCloseButton();

        myWishlistsPage.waitForCreateFormToDisappear();

        int newCount = myWishlistsPage.getWishlistCount();
        log.info("Количество списков после закрытия: {}", newCount);

        assertEquals(initialCount, newCount,
                "Количество списков не должно измениться после закрытия");
    }
}
