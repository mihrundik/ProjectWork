package tests.WishlistViewTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteLastWishlistTest extends AbstractBaseTest {

    private MyWishlistsPage myWishlistsPage;

    @Override
    protected Capabilities getOptions(String browserName) {
        switch (browserName.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                return chromeOptions;
            default:
                return null;
        }
    }

    @BeforeEach
    void initWishlistPage() {
        myWishlistsPage = page.myWishlistsPage;
    }

    @Test
    @DisplayName("Тест: Удаление первого вишлиста")
    void testDeleteFirstWishlist() {
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // снова считаем кнопки "Просмотр"
        int newCount = driver.findElements(By.xpath("//button[contains(text(), 'Просмотр')]")).size();
        log.info("Количество вишлистов после удаления: {}", newCount);

        assertEquals(initialCount - 1, newCount,
                "Количество вишлистов должно уменьшиться на 1");
    }
}