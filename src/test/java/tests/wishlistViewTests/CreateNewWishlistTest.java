package tests.wishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WaitUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CreateNewWishlistTest extends AbstractBaseTest {

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
     * 3. Заполняет форму тестовыми данными (название, описание)
     * 4. Нажимает кнопку "Создать"
     * 5. Ожидает закрытия формы и появления нового вишлиста в списке
     * 6. Проверяет, что количество вишлистов увеличилось на 1
     */
    @Test
    @DisplayName("Тест: Успешное создание нового вишлиста")
    void testCreateNewWishlist() {
        String testListName = "Тестовый список " + System.currentTimeMillis();
        String testListDescription = "Описание тестового списка для создания";

        int initialCount = myWishlistsPage.getWishlistCount();
        log.info("Начальное количество списков: {}", initialCount);

        myWishlistsPage.clickAddNewList();
        myWishlistsPage.waitForCreateFormToAppear();

        myWishlistsPage.fillCreateForm(testListName, testListDescription);
        myWishlistsPage.clickSubmitButton();

        myWishlistsPage.waitForCreateFormToDisappear();

        // Ожидаем появления нового вишлиста в списке
        By newWishlistLocator = By.xpath(String.format("//div[contains(text(), '%s')]", testListName));
        WaitUtils.waitForVisibility(driver, newWishlistLocator);

        int newCount = myWishlistsPage.getWishlistCount();
        log.info("Количество списков после создания: {}", newCount);

        assertEquals(initialCount + 1, newCount,
                "Количество списков должно увеличиться на 1");
    }

}
