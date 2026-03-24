package tests.wishlistViewTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ViewButtonsClickableTest extends AbstractBaseTest {

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
     * 1. Проверяет наличие вишлистов, при отсутствии создает новый
     * 2. Получает все карточки вишлистов
     * 3. Для каждого вишлиста проверяет, что кнопка "Просмотр" активна
     */
    @Test
    @DisplayName("Тест: Проверка кликабельности кнопок 'Просмотр'")
    void testViewButtonsAreClickable() {

        // Проверяем наличие вишлистов, при отсутствии создаем новый
        if (!myWishlistsPage.hasWishlists()) {
            log.info("Списки желаний отсутствуют. Создаём новый...");

            myWishlistsPage.clickAddNewList();
            myWishlistsPage.waitForCreateFormToAppear();

            String tempWishlistName = "Автотест-вишлист " + System.currentTimeMillis();
            String tempWishlistDesc = "Этот вишлист создан для автоматического теста";

            myWishlistsPage.fillCreateForm(tempWishlistName, tempWishlistDesc);
            myWishlistsPage.clickSubmitButton();
            myWishlistsPage.waitForCreateFormToDisappear();

            log.info("Создан временный вишлист: {}", tempWishlistName);
        }

        // Проверяем наличие списков, иначе пропускаем тест
        int wishlistCount = myWishlistsPage.getWishlistCount();
        Assumptions.assumeTrue(wishlistCount > 0, "Тест пропущен: нет списков желаний");

        // Получаем все карточки вишлистов
        List<WebElement> wishlistCards = myWishlistsPage.getAllWishlistCards();

        // Проверяем кликабельность кнопки "Просмотр" для каждого вишлиста
        for (int i = 0; i < wishlistCards.size(); i++) {
            WebElement card = wishlistCards.get(i);
            String title = card.findElement(By.xpath(".//div[contains(@class, 'card-title')]")).getText();
            WebElement viewButton = card.findElement(By.xpath(".//button[contains(text(), 'Просмотр')]"));

            assertTrue(viewButton.isEnabled(),
                    String.format("Кнопка 'Просмотр' для вишлиста '%s' должна быть активна", title));

            log.info("Кнопка для вишлиста '{}' активна", title);
        }
    }

}
