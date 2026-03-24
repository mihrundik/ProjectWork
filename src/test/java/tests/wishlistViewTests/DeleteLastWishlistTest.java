package tests.wishlistViewTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WaitUtils;
import utils.WishlistHelper;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;


public class DeleteLastWishlistTest extends AbstractBaseTest {

    private final AtomicReference<MyWishlistsPage> wishlistsPage = new AtomicReference<MyWishlistsPage>();
    private WishlistHelper wishlistHelper;

    @Override
    protected Capabilities getOptions(String browserName) {
        return super.getOptions(browserName);
    }

    @BeforeEach
    public void setUp() {
        wishlistsPage.set(new MyWishlistsPage(driver));
        wishlistHelper = new WishlistHelper(driver);
        wishlistsPage.get().open();
    }


    /**
     * Тест выполняет следующие шаги:
     * 1. Обеспечивает наличие хотя бы одного вишлиста
     * 2. Сохраняет начальное количество вишлистов
     * 3. Находит первый вишлист в списке
     * 4. Нажимает кнопку "Удалить" для этого вишлиста
     * 5. Ожидает исчезновения удаленного вишлиста из списка
     * 6. Проверяет, что количество вишлистов уменьшилось на 1
     */
    @Test
    @DisplayName("Тест: Удаление первого вишлиста")
    void testDeleteFirstWishlist() {

        // Обеспечиваем наличие вишлиста
        wishlistHelper.ensureWishlistExists();

        // Считаем кнопки "Просмотр" - это надежный счетчик уникальных вишлистов
        int initialCount = driver.findElements(By.xpath("//button[contains(text(), 'Просмотр')]")).size();

        // Проверяем наличие списков, иначе пропускаем тест
        Assumptions.assumeTrue(initialCount > 0, "Тест пропущен: нет списков желаний");

        log.info("Начальное количество вишлистов: {}", initialCount);

        // Получаем все кнопки "Просмотр"
        List<WebElement> viewButtons = driver.findElements(By.xpath("//button[contains(text(), 'Просмотр')]"));

        // Находим первый вишлист (индекс 0)
        WebElement firstCard = viewButtons.get(0).findElement(By.xpath("./ancestor::div[contains(@class, 'card')]"));
        String firstTitle = firstCard.findElement(By.xpath(".//div[contains(@class, 'card-title')]")).getText();
        log.info("Удаляем вишлист: '{}'", firstTitle);

        // Находим и кликаем кнопку удаления
        WebElement deleteButton = firstCard.findElement(By.xpath(".//button[contains(text(), 'Удалить')]"));
        deleteButton.click();
        log.info("Клик по кнопке удаления");

        // Ожидаем исчезновения удаленного вишлиста из списка
        WaitUtils.waitForInvisibility(driver, By.xpath(".//div[contains(@class, 'card-title') and text()='" + firstTitle + "']"));

        // Снова считаем кнопки "Просмотр" после удаления
        int newCount = driver.findElements(By.xpath("//button[contains(text(), 'Просмотр')]")).size();
        log.info("Количество вишлистов после удаления: {}", newCount);

        assertEquals(initialCount - 1, newCount,
                "Количество вишлистов должно уменьшиться на 1");
    }

}