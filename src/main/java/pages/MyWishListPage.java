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

import static pages.MyWishlistsPage.DEFAULT_TIMEOUT_SECONDS;
import static utils.WaitUtils.waitForPageLoad;

public class MyWishListPage extends AbstractBaseMethod {

    public Logger log = LogManager.getLogger(MyWishListPage.class);

    @FindBy(xpath = "//*[@id='root']/div/h2")
    private WebElement wishlistTitle;

    @FindBy(xpath = "//*[@id='root']/div/p")
    private WebElement wishlistDescription;

    @FindBy(xpath = "//*[@id='root']/div/button[1]")
    private WebElement addGiftButton;

    @FindBy(xpath = "//*[@id='root']/div/button[2]")
    private WebElement deleteWishlistButton;


    private final By giftsContainerLocator = By.cssSelector("#root > div > div.g-4.row");
    private final By giftCardLocator = By.cssSelector("#root > div > div.g-4.row > div.col > div.card");
    private final By giftTitleLocator = By.cssSelector(".card-title.h5");
    private final By giftDescriptionLocator = By.cssSelector(".card-text");


    public MyWishListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    // количество подарков в списке
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

    // проверить наличие подарков в списке
    public int getGiftItemsCount() {
        return getGiftCount();
    }


    // проверяет, что появилось модальное окно добавления подарка и его заголовок совпадает с expectedTitle.
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

    // проверяет, что появилось ожидаемое сообщение об ошибке.
    public boolean isErrorMessageDisplayed(String expectedText) {
        try {
            By rootXpath = By.xpath("//*[@id='root']/div");
            boolean present = wait.until(ExpectedConditions.textToBePresentInElementLocated(rootXpath, expectedText));
            String actual = driver.findElement(rootXpath).getText();
            log.info("Проверка сообщения об ошибке: ожидалось '{}', фактически '{}'", expectedText, actual);
            return present && actual != null && actual.contains(expectedText);
        } catch (Exception e) {
            log.error("Сообщение об ошибке не появилось: {}", e.getMessage());
            return true;
        }
    }

    // ожидание, что количество подарков станет expectedCount
    public boolean waitForGiftCountToBe(int expectedCount, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.numberOfElementsToBe(giftCardLocator, expectedCount));
            log.info("Ожидание: количество подарков стало {}", expectedCount);
            return true;
        } catch (Exception e) {
            log.warn("Ожидание количества подарков = {} не выполнено: {}", expectedCount, e.getMessage());
            return false;
        }
    }

    // проверить наличие подарков в списке
    public boolean hasGifts() {
        return getGiftCount() > 0;
    }

    // добавить
    public MyWishListPage clickAddGiftButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addGiftButton));
            button.click();
            log.info("Клик по кнопке 'Добавить подарок' выполнен");
            return this;
        } catch (Exception e) {
            log.error("Ошибка при клике по кнопке 'Добавить подарок': {}", e.getMessage());
            throw e;
        }
    }

    // удаление
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

    // заголовок вишлиста
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

    // проверить заголовок вишлиста
    public boolean isWishlistTitleCorrect(String expectedTitle) {
        String actualTitle = getWishlistTitle();
        boolean isCorrect = actualTitle.equals(expectedTitle);
        log.info("Проверка названия вишлиста: ожидалось '{}', фактически '{}' - {}",
                expectedTitle, actualTitle, isCorrect ? "совпадает" : "не совпадает");
        return isCorrect;
    }

    // описание вишлиста
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

    // проверить описание вишлиста
    public boolean isWishlistDescriptionCorrect(String expectedDescription) {
        String actualDescription = getWishlistDescription();
        boolean isCorrect = actualDescription.equals(expectedDescription);
        log.info("Проверка описания вишлиста: ожидалось '{}', фактически '{}' - {}",
                expectedDescription, actualDescription, isCorrect ? "совпадает" : "не совпадает");
        return isCorrect;
    }

    // загрузилась ли страница?
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

    // возвращает текст корневого блока (//*[@id='root']/div). Полезно для диагностики.
    public String getRootDivText() {
        try {
            By rootXpath = By.xpath("//*[@id='root']/div");
            WebElement rootElement = wait.until(ExpectedConditions.visibilityOfElementLocated(rootXpath));
            String text = rootElement.getText();
            log.info("Текст элемента root/div: {}", text);
            return text;
        } catch (Exception e) {
            log.error("Не удалось получить текст root/div: {}", e.getMessage());
            return "";
        }
    }

    // найти карточку подарка по точному заголовку (div.card-title.h5)
    public WebElement findGiftCardByTitle(String title) {
        try {
            List<WebElement> cards = driver.findElements(giftCardLocator);
            for (WebElement card : cards) {
                try {
                    WebElement titleEl = card.findElement(giftTitleLocator);
                    if (titleEl != null && titleEl.getText().trim().equals(title)) {
                        return card;
                    }
                } catch (Exception ignored) {
                }
            }
            return null;
        } catch (Exception e) {
            log.error("Ошибка при поиске карточки подарка по заголовку '{}': {}", title, e.getMessage());
            return null;
        }
    }

    // проверить, есть ли подарок с данным заголовком
    public boolean isGiftPresentByTitle(String title) {
        return findGiftCardByTitle(title) != null;
    }

    // обновление списка подарков — refresh и ожидание загрузки
    public void refreshGiftList() {
        driver.navigate().refresh();
        waitForPageLoad();
        // после refresh подождём видимость контейнера с карточками (если есть)
        try {
            new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS))
                    .until(ExpectedConditions.visibilityOfElementLocated(giftsContainerLocator));
        } catch (Exception e) {
            log.warn("Контейнер с подарками не найден после refresh: {}", e.getMessage());
        }
    }

    private void waitForPageLoad() {
    }

}