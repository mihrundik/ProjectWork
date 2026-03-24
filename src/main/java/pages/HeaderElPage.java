package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;


public class HeaderElPage {

    private WebDriverWait wait;

    @FindBy(xpath = "//*[@id=\"root\"]/nav/div/a")
    private WebElement navBarWL;

    @FindBy(xpath = "//*[@id=\"basic-navbar-nav\"]/div/a[1]")
    private WebElement navBarMyLists;

    @FindBy(xpath = "//*[@id=\"basic-navbar-nav\"]/div/a[2]")
    private WebElement navBarUsers;

    @FindBy(xpath = "//*[@id=\"basic-navbar-nav\"]/div/a[3]")
    private WebElement navBarLink;


    public HeaderElPage() {
    }
}