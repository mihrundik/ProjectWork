package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyWishlistsPage {

    protected Logger log = LogManager.getLogger(MyWishlistsPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final String PAGE_URL = "https://wishlist.otus.kartushin.su";

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[1]/h2")
    private WebElement pageTitle;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[1]/button")
    private WebElement addNewListButton;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]")
    private WebElement wishlistContainer;

    public MyWishlistsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        PageFactory.initElements(driver, this);

    }

    public void open() {
        driver.get(PAGE_URL);
        waitForPageToLoad();
    }

    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        log.info("Страница My Wishlists загружена");
    }

    public void clickAddNewList() {
        wait.until(ExpectedConditions.elementToBeClickable(addNewListButton)).click();
        log.info("Клик по кнопке 'Add New List'");
    }


    public List<WebElement> getWishlistCards() {
        try {
            wait.until(ExpectedConditions.visibilityOf(wishlistContainer));

            // находим все карточки с классом 'card'
            List<WebElement> allCards = wishlistContainer.findElements(By.xpath(".//div[contains(@class, 'card')]"));
            log.info("Всего карточек с классом 'card' в контейнере: {}", allCards.size());

            // используем Map для хранения уникальных вишлистов по их заголовку
            Map<String, WebElement> uniqueWishlists = new HashMap<>();

            for (WebElement card : allCards) {
                // проверяем, есть ли кнопка "Просмотр"
                List<WebElement> viewButtons = card.findElements(By.xpath(".//button[contains(text(), 'Просмотр')]"));

                if (!viewButtons.isEmpty()) {
                    // получаем заголовок вишлиста
                    String title = "";
                    try {
                        title = card.findElement(By.xpath(".//div[contains(@class, 'card-title')]")).getText();
                    } catch (Exception e) {
                        // если нет заголовка, используем текст карточки
                        title = card.getText();
                    }

                    // сохраняем только первый экземпляр каждого вишлиста
                    if (!uniqueWishlists.containsKey(title)) {
                        uniqueWishlists.put(title, card);
                        log.debug("Добавлен уникальный вишлист: {}", title);
                    } else {
                        log.debug("Пропущен дубликат вишлиста: {}", title);
                    }
                }
            }

            List<WebElement> result = new ArrayList<>(uniqueWishlists.values());
            log.info("Найдено УНИКАЛЬНЫХ вишлистов (с кнопкой Просмотр): {}", result.size());
            return result;

        } catch (Exception e) {
            log.error("Ошибка при поиске карточек вишлистов: {}", e.getMessage());
            return List.of();
        }
    }

    public int getWishlistCount() {
        return getWishlistCards().size();
    }

    public boolean hasWishlists() {
        return getWishlistCount() > 0;
    }

    public int getLastIndex() {
        return getWishlistCount() - 1;
    }

    public WebElement getLastWishlistCard() {
        List<WebElement> cards = getWishlistCards();
        if (cards.isEmpty()) {
            throw new IllegalStateException("Нет ни одной карточки. Список пуст!");
        }
        return cards.get(getLastIndex());
    }

    public void clickViewButtonOnLastWishlist() {
        WebElement lastCard = getLastWishlistCard();
        WebElement viewButton = lastCard.findElement(
                By.xpath(".//button[contains(@class, 'btn-primary') and contains(text(), 'Просмотр')]")
        );
        wait.until(ExpectedConditions.elementToBeClickable(viewButton)).click();
        log.info("Клик по кнопке 'Просмотр' для последнего списка");
    }


    public String getLastWishlistTitle() {
        WebElement lastCard = getLastWishlistCard();
        WebElement titleElement = lastCard.findElement(
                By.xpath(".//div[contains(@class, 'card-title') and contains(@class, 'h5')]")
        );
        return titleElement.getText();
    }

    public String getLastWishlistDescription() {
        WebElement lastCard = getLastWishlistCard();
        WebElement descElement = lastCard.findElement(By.xpath(".//p[@class='card-text']"));
        return descElement.getText();
    }

    public String getLastWishlistGiftCountText() {
        WebElement lastCard = getLastWishlistCard();
        WebElement countElement = lastCard.findElement(By.xpath(".//small[@class='text-muted']"));
        return countElement.getText();
    }

    // возвращаем список всех уникальных карточек вишлистов
    public List<WebElement> getAllWishlistCards() {
        return getWishlistCards();
    }


}