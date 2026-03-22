package tests.WishlistViewTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import static org.junit.jupiter.api.Assertions.*;

public class CreateWishlistWithVeryLongName extends AbstractBaseTest {

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


    @Test
    @DisplayName("Тест: Попытка создания вишлиста с очень длинным названием (512 символов) - форма не закрывается")
    void testCreateWishlistWithVeryLongDescription() {
        String testListName = "A".repeat(512);
        String testListDescription = "Описание тестового списка для создания";

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