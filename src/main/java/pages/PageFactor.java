package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class PageFactor {

    protected WebDriver driver;

    // страницы
    public HeaderElPage header;
    public MyWishlistsPage myWishlistsPage;

    public PageFactor(WebDriver driver) {
        this.driver = driver;

        // инициализация всех страниц
        header = new HeaderElPage();
        myWishlistsPage = new MyWishlistsPage(driver);

        // инициализация элементов HeaderElPage
        PageFactory.initElements(driver, header);
    }
}