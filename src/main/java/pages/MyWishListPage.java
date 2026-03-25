package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pages.MyWishlistsPage.DEFAULT_TIMEOUT_SECONDS;

public class MyWishListPage extends AbstractBaseMethod {

    public Logger log = LogManager.getLogger(MyWishListPage.class);

    @FindBy(css = "h2")
    private WebElement wishlistTitle;

    @FindBy(xpath = "//*[@id='root']/div/p")
    private WebElement wishlistDescription;

    @FindBy(xpath = "//*[@id='root']/div/button[1]")
    private WebElement addGiftButton;

    @FindBy(xpath = "//*[@id='root']/div/button[2]")
    private WebElement deleteWishlistButton;

    private final By giftCardLocator = By.cssSelector("#root > div > div.g-4.row > div.col > div.card");

    /**
     * Конструктор страницы вишлиста.
     */
    public MyWishListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    /**
     * Возвращает количество подарков в текущем вишлисте.
     */
    public int getGiftCount() {
        try {
            List<WebElement> cards = driver.findElements(giftCardLocator);
            int size = cards.size();
            log.info("Найдено карточек подарков (.card): {}", size);
            return size;
        } catch (Exception e) {
            log.error("Ошибка при подсчёте карточек подарков: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Возвращает количество подарков в списке.
     */
    public int getGiftItemsCount() {
        return getGiftCount();
    }

    /**
     * Проверяет, что появилось модальное окно добавления подарка и его заголовок соответствует ожидаемому.
     */
    public boolean isAddGiftModalDisplayedWithTitle(String expectedTitle) {
        try {
            By modalTitleXpath = By.xpath("/html/body/div[3]/div/div/div[1]/div");
            WebElement modalTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitleXpath));
            String text = modalTitleElement.getText();
            log.info("Текст модального окна добавления подарка: '{}'", text);
            return text != null && text.equals(expectedTitle);
        } catch (Exception e) {
            log.error("Модальное окно добавления подарка не появилось или произошла ошибка при чтении заголовка: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Нажимает кнопку "Добавить подарок".
     */
    public void clickAddGiftButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addGiftButton));
            button.click();
            log.info("Клик по кнопке 'Добавить подарок' выполнен");
        } catch (Exception e) {
            log.error("Ошибка при клике по кнопке 'Добавить подарок': {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Нажимает кнопку "Удалить список".
     */
    public void clickDeleteWishlistButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(deleteWishlistButton));
            button.click();
            log.info("Клик по кнопке 'Удалить список' выполнен");
        } catch (Exception e) {
            log.error("Ошибка при клике по кнопке 'Удалить список': {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Возвращает заголовок текущего вишлиста.
     */
    public String getWishlistTitle() {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOf(wishlistTitle));
            String title = element.getText();
            log.info("Получено название вишлиста: {}", title);
            return title;
        } catch (Exception e) {
            log.error("Ошибка при получении названия вишлиста: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Возвращает описание текущего вишлиста.
     */
    public String getWishlistDescription() {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOf(wishlistDescription));
            String description = element.getText();
            log.info("Получено описание вишлиста: {}", description);
            return description;
        } catch (Exception e) {
            log.error("Ошибка при получении описания вишлиста: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Проверяет, загрузилась ли страница вишлиста.
     */
    public boolean isWishlistPageDisplayed() {
        try {
            boolean isDisplayed =
                    wait.until(ExpectedConditions.visibilityOf(wishlistTitle)) != null &&
                            wait.until(ExpectedConditions.visibilityOf(wishlistDescription)) != null;
            log.info("Проверка отображения страницы вишлиста: {}", isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            log.error("Ошибка при проверке отображения страницы вишлиста: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Проверяет, что страница вишлиста загружена
     */
    public void verifyWishlistPageLoaded() {
        assertTrue(isWishlistPageDisplayed(), "Страница вишлиста не загрузилась");
    }

    /**
     * Проверяет, что страница вишлиста отображается после закрытия модального окна
     */
    public void verifyWishlistPageDisplayedAfterModalClosed() {
        assertTrue(isWishlistPageDisplayed(), "После закрытия модального окна страница вишлиста не отображается");
    }

    /**
     * Проверяет, что количество подарков увеличилось на 1
     */
    public void verifyGiftCountIncreased(int initialCount) {
        int newCount = getGiftItemsCount();
        assertEquals(initialCount + 1, newCount,
                "Количество подарков должно увеличиться на 1. Было: " + initialCount + ", стало: " + newCount);
    }

    /**
     * Проверяет, что модальное окно добавления подарка открыто с правильным заголовком
     */
    public void verifyAddGiftModalOpened() {
        assertTrue(isAddGiftModalDisplayedWithTitle("Добавить подарок"),
                "Модальное окно 'Добавить подарок' не появилось или заголовок не совпадает");
    }

    /**
     * Проверяет соответствие названия и описания вишлиста
     */
    public void verifyWishlistData(String expectedTitle, String expectedDescription) {
        String actualTitle = getWishlistTitle();
        String actualDescription = getWishlistDescription();

        assertEquals(expectedTitle, actualTitle,
                String.format("Название списка не совпадает. Ожидалось: '%s', Фактически: '%s'",
                        expectedTitle, actualTitle));

        assertEquals(expectedDescription, actualDescription,
                String.format("Описание списка не совпадает. Ожидалось: '%s', Фактически: '%s'",
                        expectedDescription, actualDescription));

        log.info("Проверка данных вишлиста успешно выполнена");
    }

    /**
     * Проверяет, что кнопка "Добавить подарок" кликабельна
     */
    public void verifyAddGiftButtonClickable() {
        wait.until(ExpectedConditions.elementToBeClickable(addGiftButton));
        // Если дошли до этой строки, значит элемент кликабельный
        log.info("Кнопка 'Добавить подарок' кликабельна");
    }

    /**
     * Проверяет, что кнопка "Удалить список" кликабельна
     */
    public void verifyDeleteWishlistButtonClickable() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteWishlistButton));
        // Если дошли до этой строки, значит элемент кликабельный
        log.info("Кнопка 'Удалить список' кликабельна");
    }

}
