package pages;

import org.openqa.selenium.WebDriver;

public class PageFactory {

    protected WebDriver driver;

    // страницы
    public HeaderElPage header;
    public MyWishlistsPage myWishlistsPage;

    public PageFactory(WebDriver driver) {
        this.driver = driver;

        // инициализация всех страниц
        header = new HeaderElPage();
        myWishlistsPage = new MyWishlistsPage(driver);

        // инициализация элементов HeaderElPage
        org.openqa.selenium.support.PageFactory.initElements(driver, header);
    }
}