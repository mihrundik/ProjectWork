package pages;

import org.openqa.selenium.WebDriver;

public class PageFactory {

    protected WebDriver driver;

    // Страницы
    public HeaderElPage header;
    public MyWishlistsPage myWishlistsPage;

    /**
     * Конструктор фабрики страниц.
     * Инициализирует все страницы приложения и их элементы.
     */
    public PageFactory(WebDriver driver) {
        this.driver = driver;

        // Инициализация всех страниц
        header = new HeaderElPage();
        myWishlistsPage = new MyWishlistsPage(driver);

        // Инициализация элементов HeaderElPage
        org.openqa.selenium.support.PageFactory.initElements(driver, header);
    }
}