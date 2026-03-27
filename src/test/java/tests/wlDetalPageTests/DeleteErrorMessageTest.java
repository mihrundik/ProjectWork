package tests.wlDetalPageTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.Capabilities;
import pages.MyWishListPage;
import pages.MyWishlistsPage;
import tests.AbstractBaseTest;
import utils.WaitUtils;
import utils.WishlistHelper;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class DeleteErrorMessageTest extends AbstractBaseTest {

    private MyWishlistsPage wishlistsPage;
    private WishlistHelper wishlistHelper;

    @Override
    protected Capabilities getOptions(String browserName) {
        return super.getOptions(browserName);
    }

    @BeforeEach
    public void setUp() {
        wishlistsPage = new MyWishlistsPage(driver);
        wishlistHelper = new WishlistHelper(driver);
        wishlistsPage.open();
    }

    /**
     * Тест выполняет следующие шаги:
     * 1. Обеспечивает наличие хотя бы одного вишлиста
     * 2. Переходит к последнему вишлисту
     * 3. Дважды нажимает кнопку "Удалить список" (имитация ошибки)
     * 4. Проверяет, что отображается ожидаемое сообщение об ошибке
     */
    @Test
    @DisplayName("Кнопка 'Удалить список' при ошибке показывает сообщение 'Ошибка: Ошибка при загрузке списка желаний'")
    public void deleteWishlistShowsErrorMessage() {
        // Обеспечиваем наличие вишлиста
        wishlistHelper.ensureWishlistExists();

        // Переходим к последнему вишлисту
        wishlistsPage.clickViewButtonOnLastWishlist();
        MyWishListPage wishListPage = new MyWishListPage(driver);

        // Проверяем, что страница вишлиста загрузилась
        wishListPage.assertions().verifyWishlistPageLoaded();

        // Проверяем, что кнопка "Удалить список" кликабельна
        wishListPage.assertions().verifyDeleteWishlistButtonClickable();

        // Нажимаем кнопку "Удалить список" дважды для имитации ошибки
        wishListPage.clickDeleteWishlistButton();
        wishListPage.clickDeleteWishlistButton();

        // Переинициализируем страницу вишлиста
        wishListPage = new MyWishListPage(driver);

        // Ждём появления любого элемента с текстом 'Ошибка' (используем ваш WaitUtils)
        String expectedFragmentPrimary = "Ошибка при загрузке списка желаний";
        String expectedVariant = "Не удалось удалить список желаний";
        String actualText = "";

        try {
            // Ждём, пока появится элемент, содержащий слово "Ошибка"
            WaitUtils.getWait(driver).until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("//*[contains(normalize-space(.), 'Ошибка')]")
            ));

            // Пытаемся взять элемент, содержащий более точный фрагмент (предпочтительно)
            try {
                org.openqa.selenium.WebElement el = driver.findElement(
                        org.openqa.selenium.By.xpath("//*[contains(normalize-space(.), '" + expectedFragmentPrimary + "')]")
                );
                actualText = el.getText();
            } catch (Exception e1) {
                // Иначе ищем элемент с вариантом "Не удалось удалить..."
                try {
                    org.openqa.selenium.WebElement el = driver.findElement(
                            org.openqa.selenium.By.xpath("//*[contains(normalize-space(.), '" + expectedVariant + "')]")
                    );
                    actualText = el.getText();
                } catch (Exception e2) {
                    // В fallback читаем корневой блок
                    try {
                        org.openqa.selenium.WebElement root = driver.findElement(org.openqa.selenium.By.xpath("//*[@id='root']/div"));
                        actualText = root.getText();
                    } catch (Exception e3) {
                        actualText = "";
                    }
                }
            }
        } catch (org.openqa.selenium.TimeoutException toe) {
            log.warn("Не нашли элемент с 'Ошибка' за отведённое время: {}", toe.getMessage());
        }

        log.info("Фактический текст после попытки удаления: '{}'", actualText);

        boolean containsExpected =
                actualText != null && (actualText.contains(expectedFragmentPrimary) || actualText.contains(expectedVariant));

        assertTrue(containsExpected,
                String.format("Ожидался один из фрагментов: ['%s', '%s'], но фактический текст: '%s'",
                        expectedFragmentPrimary, expectedVariant, actualText));
    }
}