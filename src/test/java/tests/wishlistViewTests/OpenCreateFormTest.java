package tests.wishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class OpenCreateFormTest extends AbstractBaseTest {

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
     * 1. Нажимает кнопку "Добавить список"
     * 2. Ожидает появления формы создания
     * 3. Переинициализирует элементы модального окна
     * 4. Проверяет, что поле названия отображается
     * 5. Проверяет, что поле описания отображается
     * 6. Закрывает форму для очистки состояния
     */
    @Test
    @DisplayName("Тест: Открытие формы создания нового вишлиста")
    void testOpenCreateForm() {
        myWishlistsPage.clickAddNewList();
        myWishlistsPage.waitForCreateFormToAppear();

        // Переинициализируем элементы модального окна
        myWishlistsPage.initModalElements();

        // Проверяем отображение полей формы
        assertTrue(myWishlistsPage.getNameNewWL().isDisplayed(),
                "Поле названия должно отображаться");
        assertTrue(myWishlistsPage.getDescriptionNewWL().isDisplayed(),
                "Поле описания должно отображаться");

        // Закрываем форму, чтобы не влиять на другие тесты
        myWishlistsPage.clickCloseButton();
        myWishlistsPage.waitForCreateFormToDisappear();

        log.info("Форма создания успешно открылась и закрылась");
    }

}
