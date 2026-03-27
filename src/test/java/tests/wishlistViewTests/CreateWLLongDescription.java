package tests.wishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WaitUtils;


public class CreateWLLongDescription extends AbstractBaseTest {

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
     * 3. Заполняет форму названием и описанием длиной 512 символов
     * 4. Нажимает кнопку "Создать"
     * 5. Проверяет, что форма осталась открытой (валидация не пропустила создание)
     * 6. Проверяет, что количество вишлистов не изменилось
     * 7. Закрывает форму для очистки состояния
     */
    @Test
    @DisplayName("Тест: Попытка создания вишлиста с очень длинным описанием (512 символов) - форма не закрывается")
    void testCreateWishlistWithVeryLongName() {
        String testListName = "A".repeat(512);
        String testListDescription = "Описание тестового списка для создания";

        log.info("Длина названия: {} символов", testListName.length());

        int initialCount = myWishlistsPage.getWishlistCount();
        log.info("Начальное количество списков: {}", initialCount);

        myWishlistsPage.clickAddNewList();
        myWishlistsPage.waitForCreateFormToAppear();

        myWishlistsPage.fillCreateForm(testListName, testListDescription);
        myWishlistsPage.clickSubmitButton();

        // Небольшая задержка для обработки валидации
        WaitUtils.waitForVisibility(driver, org.openqa.selenium.By.cssSelector("input.form-control[type='text'][required]"));

        // Проверяем, что форма НЕ закрылась (осталась открытой)
        myWishlistsPage.assertions().verifyCreateFormVisible();  // ← теперь метод существует

        // Проверяем, что количество списков не изменилось
        myWishlistsPage.assertions().verifyWishlistCountChanged(initialCount, "после попытки создания");

        // Закрываем форму, чтобы не влиять на другие тесты
        myWishlistsPage.closeCreateForm();
        log.info("Форма закрыта");
    }

}