package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static pages.MyWishlistsPage.DEFAULT_TIMEOUT_SECONDS;

public class AddGiftPage extends AbstractBaseMethod {

    @FindBy(css = "input[name='name'], input.gift-name")
    private WebElement giftNameField;

    @FindBy(css = "textarea[name='description'], textarea.gift-description")
    private WebElement giftDescriptionField;

    @FindBy(css = "input[name='price'], input.gift-price")
    private WebElement giftPriceField;

    @FindBy(css = "input[name='url'], input.gift-url")
    private WebElement giftUrlField;

    @FindBy(css = "button[type='submit'], button.save-gift")
    private WebElement saveButton;

    @FindBy(css = "button.cancel, a.cancel")
    private WebElement cancelButton;

    @FindBy(css = ".current-wishlist-name")
    private WebElement currentWishlistName;


    public AddGiftPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        PageFactory.initElements(driver, this);
    }


}