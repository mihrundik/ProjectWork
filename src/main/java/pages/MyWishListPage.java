package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static pages.MyWishlistsPage.DEFAULT_TIMEOUT_SECONDS;

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

    @FindBy(xpath = "//*[@id='root']/div/div")
    private List<WebElement> giftItems;

    public MyWishListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    // количество подарков в списке
    public int getGiftItemsCount() {
        try {
            int count = giftItems.size();
            log.info("Количество подарков в списке: {}", count);
            return count;
        } catch (Exception e) {
            log.error("Ошибка при получении количества подарков: {}", e.getMessage());
            return 0;
        }
    }

    // проверить наличие подарков в списке
    public boolean hasGifts() {
        return getGiftItemsCount() > 0;
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

}