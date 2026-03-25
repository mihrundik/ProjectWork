package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Класс элементов навигационной панели страницы.
 */
public class HeaderElPage {

    private WebDriverWait wait;

    @FindBy(css = ".navbar-brand")
    private WebElement navBarWL;

    @FindBy(css = "a[href=\"/wishlists\"]")
    private WebElement navBarMyLists;

    @FindBy(css = "a[href=\"/users\"]")
    private WebElement navBarUsers;

    @FindBy(css = "a[role=\"button\"]")
    private WebElement navBarLink;


    public HeaderElPage() {
    }
}