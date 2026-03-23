package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static pages.MyWishlistsPage.DEFAULT_TIMEOUT_SECONDS;


public class AddGiftPage extends AbstractBaseMethod {

    @FindBy(css = "h2")
    private WebElement pageTitle;

    @FindBy(css = "input[type=\"text\"][required]")
    private WebElement giftNameField;

    @FindBy(css = "textarea[required]")
    private WebElement giftDescriptionField;

    @FindBy(css = "input[placeholder=\"https://example.com/product\"]")
    private WebElement giftUrlProdact;

    @FindBy(css = "input[type=\"number\"]")
    private WebElement giftPriceProdact;

    @FindBy(css = "input[placeholder=\"https://example.com/image.jpg\"]")
    private WebElement giftUrlImage;

    @FindBy(css = "button.btn-primary[type=\"submit\"]")
    private WebElement saveButton;

    @FindBy(css = ".btn-close")
    private WebElement cancelButton;


    public AddGiftPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    public WebElement getGiftNameField() {
        return giftNameField;
    }

    public WebElement getGiftDescriptionField() {
        return giftDescriptionField;
    }

    public WebElement giftUrlProdact() {
        return giftUrlProdact;
    }

    public WebElement giftPriceProdact() {
        return giftPriceProdact;
    }

    public WebElement giftUrlImage() {
        return giftUrlImage;
    }

    public WebElement getSaveButton() {
        return saveButton;
    }

    public WebElement getCancelButton() {
        return cancelButton;
    }


    // локатор для ожиданий (для видимость/невидимость)
    private final By cancelButtonLocator = By.cssSelector("button.cancel, a.cancel");
    // контейнер модалки
    public final By modalRootLocator = By.xpath("/html/body/div[3]");


    // ожидание появления модального окна
    public boolean waitForModalToAppear() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(cancelButtonLocator),
                    ExpectedConditions.visibilityOf(saveButton),
                    ExpectedConditions.visibilityOfElementLocated(modalRootLocator)
            ));
            log.info("Модальное окно добавления подарка появилось");
            return true;
        } catch (Exception e) {
            log.error("Модальное окно не появилось: {}", e.getMessage());
            return false;
        }
    }

    // клик по крестику
    public void clickCancelButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
            button.click();
            log.info("Нажата кнопка отмены (крестик) в модальном окне добавления подарка");
        } catch (Exception e) {
            log.error("Не удалось нажать кнопку отмены в модальном окне: {}", e.getMessage());
            throw e;
        }
    }

    // ожидание закрытия модального окна
    public boolean waitForModalToDisappear() {
        try {
            boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(modalRootLocator));
            if (invisible) {
                log.info("Модальное окно закрылось (контейнер невидим)");
                return true;
            } else {
                // доп проверка — убеждаемся, что кнопка отмены тоже невидима
                boolean cancelInvisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(cancelButtonLocator));
                log.info("Модальное окно закрыто по проверке cancelButton: {}", cancelInvisible);
                return cancelInvisible;
            }
        } catch (Exception e) {
            log.error("Модальное окно не закрылось за отведённое время: {}", e.getMessage());
            return false;
        }
    }

    // на всякий случай проверка, что модалка сейчас отображается
    public boolean isModalDisplayed() {
        try {
            // используем тот же локатор контейнера, что и для ожидания закрытия - гарантирует, что мы проверяем именно тот элемент
            return driver.findElement(modalRootLocator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // клик на кнопку Добавить
    public void clickSaveButton() {
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        saveButton.click();
        log.info("Нажата кнопка 'Добавить' в модальном окне");
    }


}