package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;


public class LoginPage {

    private WebDriver driver;

    @FindBy(xpath = "//*[@id=\"root\"]/div/form/div[1]/input")
    private WebElement formControlName;

    @FindBy(xpath = "//*[@id=\"root\"]/div/form/div[2]/input")
    private WebElement formControlPass;

    @FindBy(xpath = "//*[@id=\"root\"]/div/form/button")
    private WebElement buttonEnter;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

   // выполнение входа в сиситему
   public void login(String login, String password) {
       WaitUtils.waitForVisibility(driver, formControlName).sendKeys(login);
       formControlPass.sendKeys(password);
       buttonEnter.click();
   }

    // проверка что мы находимся на странице авторизации
    public boolean isLoginPageDisplayed() {
        try {
            return WaitUtils.waitForVisibility(driver, buttonEnter).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement getFormControlName() {
        return formControlName;
    }

    public WebElement getFormControlPass() {
        return formControlPass;
    }

    public WebElement getButtonEnter() {
        return buttonEnter;
    }
}