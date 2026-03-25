package tests.wishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;


public class CancelCreationTest extends AbstractBaseTest {

    private MyWishlistsPage myWishlistsPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        return super.getOptions(browserName);
    }

    @BeforeEach
    void initWishlistPage() {
        // инициализация определенной страницы
        myWishlistsPage = page.myWishlistsPage;
    }


    /**
     * Тест выполняет следующие шаги:
     * 1. Сохраняет начальное количество вишлистов
     * 2. Открывает форму создания вишлиста
     * 3. Заполняет форму тестовыми данными
     * 4. Нажимает кнопку "Отмена"
     * 5. Проверяет, что форма закрылась
     * 6. Проверяет, что количество вишлистов не изменилось
     */
    @Test
    @DisplayName("Тест: Отмена создания через кнопку 'Отмена'")
    void testCancelCreation() {
        String testListName = "Тестовый список " + System.currentTimeMillis();
        String testListDescription = "Описание тестового списка для отмены";

        int initialCount = myWishlistsPage.getWishlistCount();
        log.info("Начальное количество списков: {}", initialCount);

        myWishlistsPage.clickAddNewList();
        myWishlistsPage.waitForCreateFormToAppear();

        myWishlistsPage.fillCreateForm(testListName, testListDescription);
        myWishlistsPage.clickCancelButton();

        myWishlistsPage.waitForCreateFormToDisappear();

        // Проверяем, что количество вишлистов не изменилось
        myWishlistsPage.verifyWishlistCountChanged(initialCount, "после отмены");
        myWishlistsPage.verifyCreateFormNotVisible();
    }

}
