package pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


@Slf4j
public class MyWishlistsPage {

    // WebDriver и ожидания
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Константы для таймаутов
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final int POLLING_INTERVAL_MILLIS = 500;

    private static final String BASE_URL = "https://wishlist.otus.kartushin.su";
    private static final String PAGE_PATH = "/wishlist";

    // Wishlist элементы
    @FindBy(xpath = "//*[@id=\"root\"]/div/div[1]/h2")
    private WebElement pageTitle;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[1]/button")
    private WebElement addNewList;


    // массив для элементов списков желаний
    @FindBy(xpath = "//h2/ancestor::div[contains(@class, 'card')] | //div[contains(@class, 'wishlist-item')]")
    private List<WebElement> allWishlistCards;

    public MyWishlistsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    // получаем размер массива
    public int getWishlistCount() {
        // allWishlistCards - это List<WebElement>, у него есть метод size()
        return allWishlistCards.size();
    }

    // проверка что список не пустой
    public boolean hasWishlists() {
        return getWishlistCount() > 0;
    }

    // проверить что список пустой
    public boolean isEmpty() {
        return getWishlistCount() == 0;
    }


    /**
     * Получить индекс последнего элемента
     *
     * @return индекс последнего элемента или -1 если список пуст
     */
    public int getLastIndex() {
        return getWishlistCount() - 1;
    }

    /**
     * Получить последнюю карточку (WebElement)
     *
     * @return WebElement последней карточки
     * @throws IllegalStateException если список пуст
     */
    public WebElement getLastWishlistCard() {
        if (isEmpty()) {
            throw new IllegalStateException("Нет ни одной карточки. Список пуст!");
        }

        int lastIndex = getLastIndex();
        log.info("Получаем последнюю карточку с индексом: {}", lastIndex);
        return allWishlistCards.get(lastIndex);
    }

    /**
     * получаем название последнего списка
     * <div class="card-title h5">текст</div>
     */
    public String getLastWishlistTitle() {
        WebElement lastCard = getLastWishlistCard();

        // Ищем внутри последней карточки div с классами card-title и h5
        WebElement titleElement = lastCard.findElement(
                By.xpath(".//div[contains(@class, 'card-title') and contains(@class, 'h5')]")
        );

        String title = titleElement.getText();
        log.info("Заголовок последнего списка: '{}'", title);

        return title;
    }

    /**
     * получаем описание последнего списка
     * <p class="card-text">треьт=ий список желаний</p>
     */
    public String getLastWishlistDescription() {
        WebElement lastCard = getLastWishlistCard();
        WebElement descElement = lastCard.findElement(
                By.xpath(".//p[@class='card-text']")
        );

        String description = descElement.getText();
        log.info("Описание последнего списка: '{}'", description);

        return description;
    }

    /**
     * кликнуть на кнопку "Просмотр" последнего списка
     */
    public WebElement getLastWishlistViewButton() {
        WebElement lastCard = getLastWishlistCard();
        return lastCard.findElement(
                By.xpath(".//button[contains(@class, 'btn') and contains(@class, 'btn-primary') and contains(text(), 'Просмотр')]")
        );
    }

    /**
     * кликнуть на кнопку "Удалить" последнего списка
     */
    private WebElement getLastWishlistDeleteButton() {
        WebElement lastCard = getLastWishlistCard();
        return lastCard.findElement(
                By.xpath(".//button[contains(@class, 'btn') and contains(@class, 'btn-primary') and contains(text(), 'Удалить')]")
        );
    }

    /**
     * получение информации о количестве подарков на карточке
     */
    public String getLastWishlistGiftCountText() {
        WebElement lastCard = getLastWishlistCard();

        WebElement countElement = lastCard.findElement(
                By.xpath(".//small[@class='text-muted']")
        );

        String text = countElement.getText();
        log.info("Текст количества подарков: '{}'", text);

        return text;
    }


}



