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

    @Test
    @DisplayName("Тест: Проверка кликабельности кнопок 'Просмотр'")
    void testViewButtonsAreClickable() {

// Шаг 1: проверяем наличие списков на уже загруженной странице
        if (!myWishlistsPage.hasWishlists()) {
            log.info("Списки желаний отсутствуют. Создаём новый...");

            // Шаг 1.1: вызываем метод на существующем объекте страницы.
            myWishlistsPage.clickAddNewList();

            // ждём появления формы (используем улучшенный метод)
            myWishlistsPage.waitForCreateFormToAppear(); // Теперь это работает корректно!

            String tempWishlistName = "Автотест-вишлист " + System.currentTimeMillis();
            String tempWishlistDesc = "Этот вишлист создан для автоматического теста";

            myWishlistsPage.fillCreateForm(tempWishlistName, tempWishlistDesc);
            myWishlistsPage.clickSubmitButton();

            // ждём исчезновение формы
            myWishlistsPage.waitForCreateFormToDisappear();
            log.info("Создан временный вишлист: {}", tempWishlistName);
        }

        int wishlistCount = myWishlistsPage.getWishlistCount();
        Assumptions.assumeTrue(wishlistCount > 0, "Тест пропущен: нет списков желаний");

        List<WebElement> wishlistCards = myWishlistsPage.getAllWishlistCards();

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
