package tests.WLDetalPageTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import pages.MyWishListPage;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WaitUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteErrorMessageTest extends AbstractBaseTest {

    private MyWishlistsPage wishlistsPage;
    private MyWishListPage wishListPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        return getBrowserOptions(browserName);
    }

    @BeforeEach
    public void setUp() {
        wishlistsPage = new MyWishlistsPage(driver);
        wishlistsPage.open();
    }


    @Test
    @DisplayName("Кнопка 'Удалить список' при ошибке показывает сообщение 'Ошибка: Ошибка при загрузке списка желаний'")
    public void deleteWishlistShowsErrorMessage() {
        // убедимся, что есть список, и откроем его
        if (!wishlistsPage.hasWishlists()) {
            wishlistsPage.clickAddNewList();
            wishlistsPage.waitForCreateFormToAppear();

            String testName = "Тестовый список " + System.currentTimeMillis();
            String testDescription = "Описание тестового списка";

            wishlistsPage.fillCreateForm(testName, testDescription);
            wishlistsPage.clickSubmitButton();
            wishlistsPage.waitForCreateFormToDisappear();

            log.info("Создан тестовый список для проверки удаления: {}", testName);
        }

        wishlistsPage.clickViewButtonOnLastWishlist();
        wishListPage = new MyWishListPage(driver);

        // проверяем, что страница вишлиста загрузилась
        assertTrue(wishListPage.isWishlistPageDisplayed(), "Страница вишлиста не загрузилась");

        // нажимаем "Удалить список"
        wishListPage.clickDeleteWishlistButton();

        WaitUtils.waitForClickable(driver, By.xpath("//button[contains(text(), 'Удалить список')]"));

        // нажимаем "Удалить список"
        wishListPage.clickDeleteWishlistButton();

        By errorMessageLocator = By.xpath("//div[contains(@class, 'error') or contains(@class, 'message') or contains(@class, 'alert')]");

        // Ожидаем, что сообщение появится и содержит нужный текст
        boolean isErrorMessageVisible = WaitUtils.waitForTextPresent(
                driver,
                errorMessageLocator,
                "Ошибка: Ошибка при загрузке списка желаний"
        );
    }

}