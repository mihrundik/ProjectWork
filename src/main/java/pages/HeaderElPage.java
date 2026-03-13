package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HeaderElPage {

    // Header elements
    @FindBy(xpath = "//*[@id=\"root\"]/nav/div/a")
    private WebElement navBarWL;

    @FindBy(xpath = "//*[@id=\"root\"]/nav/div/a")
    private WebElement navBarMyLists;

    @FindBy(xpath = "//*[@id=\"basic-navbar-nav\"]/div/a[2]")
    private WebElement navBarUsers;

    @FindBy(xpath = "//*[@id=\"basic-navbar-nav\"]/div/a[3]")
    private WebElement navBarLink;

}
