package tests.WishlistViewTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WaitUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class DeleteLastWishlistTest extends AbstractBaseTest {

    private MyWishlistsPage wishlistsPage;

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
    @DisplayName("Тест: Удаление первого вишлиста")
    void testDeleteFirstWishlist() {
        // Шаг 1: проверяем наличие списков на уже загруженной странице
        if (!wishlistsPage.hasWishlists()) {
            log.info("Списки желаний отсутствуют. Создаём новый...");

            // Шаг 1.1: вызываем метод на существующем объекте страницы.
            wishlistsPage.clickAddNewList();

            // ждём появления формы (используем улучшенный метод)
            wishlistsPage.waitForCreateFormToAppear(); // Теперь это работает корректно!

            String tempWishlistName = "Автотест-вишлист " + System.currentTimeMillis();
            String tempWishlistDesc = "Этот вишлист создан для автоматического теста";

            wishlistsPage.fillCreateForm(tempWishlistName, tempWishlistDesc);
            wishlistsPage.clickSubmitButton();

            // ждём исчезновение формы
            wishlistsPage.waitForCreateFormToDisappear();
            log.info("Создан временный вишлист: {}", tempWishlistName);
        }
        // считаем кнопки "Просмотр" - это самые надежный счетчик уникальных вишлистов
        int initialCount = driver.findElements(By.xpath("//button[contains(text(), 'Просмотр')]")).size();

        Assumptions.assumeTrue(initialCount > 0, "Тест пропущен: нет списков желаний");

        log.info("Начальное количество вишлистов: {}", initialCount);

        // получаем все кнопки "Просмотр"
        List<WebElement> viewButtons = driver.findElements(By.xpath("//button[contains(text(), 'Просмотр')]"));

        // берем первый вишлист (индекс 0)
        WebElement firstCard = viewButtons.get(0).findElement(By.xpath("./ancestor::div[contains(@class, 'card')]"));
        String firstTitle = firstCard.findElement(By.xpath(".//div[contains(@class, 'card-title')]")).getText();
        log.info("Удаляем вишлист: '{}'", firstTitle);

        // находим и кликаем кнопку удаления
        WebElement deleteButton = firstCard.findElement(By.xpath(".//button[contains(text(), 'Удалить')]"));
        deleteButton.click();
        log.info("Клик по кнопке удаления");

        // ждем обновления списка
        WaitUtils.waitForInvisibility(driver, By.xpath(".//div[contains(@class, 'card-title') and text()='" + firstTitle + "']"));

        // снова считаем кнопки "Просмотр"
        int newCount = driver.findElements(By.xpath("//button[contains(text(), 'Просмотр')]")).size();
        log.info("Количество вишлистов после удаления: {}", newCount);

        assertEquals(initialCount - 1, newCount,
                "Количество вишлистов должно уменьшиться на 1");
    }
}