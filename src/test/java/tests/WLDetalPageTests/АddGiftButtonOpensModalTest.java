package tests.WLDetalPageTests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishListPage;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class АddGiftButtonOpensModalTest extends AbstractBaseTest {

    private MyWishlistsPage wishlistsPage;
    private MyWishListPage wishListPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        return super.getOptions(browserName);
    }

    @BeforeEach
    public void setUp() {
        wishlistsPage = new MyWishlistsPage(driver);
        wishlistsPage.open();
    }


    @Test
    @DisplayName("Кнопка 'Добавить подарок' открывает модальное окно с заголовком 'Добавить подарок'")
    public void addGiftButtonOpensModal() {
        // убедимся, что есть список, и откроем его
        if (!wishlistsPage.hasWishlists()) {
            wishlistsPage.clickAddNewList();
            wishlistsPage.waitForCreateFormToAppear();

            String testName = "Тестовый список " + System.currentTimeMillis();
            String testDescription = "Описание тестового списка";

            wishlistsPage.fillCreateForm(testName, testDescription);
            wishlistsPage.clickSubmitButton();
            wishlistsPage.waitForCreateFormToDisappear();

            log.info("Создан тестовый список для проверки модалки: {}", testName);
        }

        wishlistsPage.clickViewButtonOnLastWishlist();
        wishListPage = new MyWishListPage(driver);

        // проверяем, что страница вишлиста загрузилась
        assertTrue(wishListPage.isWishlistPageDisplayed(), "Страница вишлиста не загрузилась");

        // нажимаем кнопку "Добавить подарок"
        wishListPage.clickAddGiftButton();

        // ожидаем появления модального окна с заголовком "Добавить подарок"
        assertTrue(wishListPage.isAddGiftModalDisplayedWithTitle("Добавить подарок"),
                "Модальное окно 'Добавить подарок' не появилось или заголовок не совпадает");
    }

}