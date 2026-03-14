package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HeaderElPage {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//*[@id=\"root\"]/nav/div/a")
    private WebElement navBarWL;

    @FindBy(xpath = "//*[@id=\"basic-navbar-nav\"]/div/a[1]")
    private WebElement navBarMyLists;

    @FindBy(xpath = "//*[@id=\"basic-navbar-nav\"]/div/a[2]")
    private WebElement navBarUsers;

    @FindBy(xpath = "//*[@id=\"basic-navbar-nav\"]/div/a[3]")
    private WebElement navBarLink;

    public HeaderElPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // переходит на страницу "Мои списки" (которая ведет на страницу логина)
    public void goToMyLists() {
        wait.until(ExpectedConditions.elementToBeClickable(navBarMyLists)).click();
    }

    public WebElement getNavBarWL() {
        return navBarWL;
    }

    public WebElement getNavBarMyLists() {
        return navBarMyLists;
    }

    public WebElement getNavBarUsers() {
        return navBarUsers;
    }

    public WebElement getNavBarLink() {
        return navBarLink;
    }

    public HeaderElPage() {
    }
}