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

    @FindBy(xpath = "/html/body/div[3]/div/div/div[2]/form/div[1]/input")
    private WebElement nameNewWL;

    @FindBy(xpath = "/html/body/div[3]/div/div/div[2]/form/div[2]/textarea")
    private WebElement descriptionNewWL;

    @FindBy(xpath = "/html/body/div[3]/div/div/div[1]/button")
    private WebElement closeButton;

    @FindBy(xpath = "/html/body/div[3]/div/div/div[2]/form/div[3]/button[1]")
    private WebElement cancelButton;

    @FindBy(xpath = "/html/body/div[3]/div/div/div[2]/form/div[3]/button[2]")
    private WebElement submitButton;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[3]/div/div/div[2]/button")
    private WebElement delButton;

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


    // методы работы с формой создания вишлиста
    public void waitForCreateFormToAppear() {
        wait.until(ExpectedConditions.visibilityOf(nameNewWL));
        log.info("Форма создания вишлиста появилась");
    }

    public void waitForCreateFormToDisappear() {
        wait.until(ExpectedConditions.invisibilityOf(nameNewWL));
        log.info("Форма создания вишлиста закрылась");
    }

    public void fillCreateForm(String name, String description) {
        nameNewWL.clear();
        nameNewWL.sendKeys(name);
        log.info("Заполнено название: {}", name);

        if (description != null && !description.isEmpty()) {
            descriptionNewWL.clear();
            descriptionNewWL.sendKeys(description);
            log.info("Заполнено описание: {}", description);
        }
    }

    public void clickCancelButton() {
        wait.until(ExpectedConditions.elementToBeClickable(cancelButton)).click();
        log.info("Клик по кнопке 'Отмена'");
    }

    public void clickCloseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
        log.info("Клик по кнопке закрытия (крестик)");
    }

    public void clickSubmitButton() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        log.info("Клик по кнопке 'Создать'");
    }

    public WebElement getNameNewWL() {
        try {
            log.info("Поиск поля названия по XPath: /html/body/div[3]/div/div/div[2]/form/div[1]/input");

            // Сначала проверим, есть ли вообще модальное окно
            List<WebElement> modals = driver.findElements(By.xpath("/html/body/div[3]"));
            log.info("Найдено модальных окон: {}", modals.size());

            if (modals.isEmpty()) {
                log.error("Модальное окно не найдено!");
                return null;
            }

            // Теперь ищем поле ввода
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/div[3]/div/div/div[2]/form/div[1]/input")
            ));
            log.info("Поле названия найдено, видимо: {}", element.isDisplayed());
            return element;
        } catch (TimeoutException e) {
            log.error("Таймаут при поиске поля названия: {}", e.getMessage());

            // Отладочная информация
            try {
                String pageSource = driver.getPageSource();
                log.info("Первые 500 символов страницы: {}", pageSource.substring(0, Math.min(500, pageSource.length())));
            } catch (Exception ex) {
                log.error("Не удалось получить page source");
            }
            return null;
        } catch (Exception e) {
            log.error("Ошибка при поиске поля названия: {}", e.getMessage());
            return null;
        }
    }

    public WebElement getDescriptionNewWL() {
        try {
            log.info("Поиск поля описания по XPath: /html/body/div[3]/div/div/div[2]/form/div[2]/textarea");

            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/div[3]/div/div/div[2]/form/div[2]/textarea")
            ));
            log.info("Поле описания найдено, видимо: {}", element.isDisplayed());
            return element;
        } catch (Exception e) {
            log.error("Ошибка при поиске поля описания: {}", e.getMessage());
            return null;
        }
    }

    // поля инициализируются один раз при создании объекта страницы,
    // но если элемент появляется позже (в модальном окне), то поле остается null
    // поэтому инициализируем повторно
    public void initModalElements() {
        PageFactory.initElements(driver, this);
        log.info("Элементы модального окна переинициализированы");
    }

    public String getWishlistTitleByIndex(int index) {
        List<WebElement> cards = getWishlistCards();
        if (index >= 0 && index < cards.size()) {
            WebElement card = cards.get(index);
            return card.findElement(By.xpath(".//div[contains(@class, 'card-title')]")).getText();
        }
        throw new IndexOutOfBoundsException("Нет вишлиста с индексом " + index);
    }

    public String getWishlistDescriptionByIndex(int index) {
        List<WebElement> cards = getWishlistCards();
        if (index >= 0 && index < cards.size()) {
            WebElement card = cards.get(index);
            return card.findElement(By.xpath(".//p[@class='card-text']")).getText();
        }
        throw new IndexOutOfBoundsException("Нет вишлиста с индексом " + index);
    }

    public String getWishlistGiftCountByIndex(int index) {
        List<WebElement> cards = getWishlistCards();
        if (index >= 0 && index < cards.size()) {
            WebElement card = cards.get(index);
            return card.findElement(By.xpath(".//small[@class='text-muted']")).getText();
        }
        throw new IndexOutOfBoundsException("Нет вишлиста с индексом " + index);
    }

    public WebElement getCloseButton() {
        try {
            log.info("Поиск кнопки закрытия");
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("/html/body/div[3]/div/div/div[1]/button")
            ));
            log.info("Кнопка закрытия найдена");
            return element;
        } catch (Exception e) {
            log.error("Ошибка при поиске кнопки закрытия: {}", e.getMessage());
            return null;
        }
    }

    public WebElement getCancelButton() {
        try {
            log.info("Поиск кнопки отмены");
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("/html/body/div[3]/div/div/div[2]/form/div[3]/button[1]")
            ));
            log.info("Кнопка отмены найдена");
            return element;
        } catch (Exception e) {
            log.error("Ошибка при поиске кнопки отмены: {}", e.getMessage());
            return null;
        }
    }

    public WebElement getSubmitButton() {
        try {
            log.info("Поиск кнопки создания");
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("/html/body/div[3]/div/div/div[2]/form/div[3]/button[2]")
            ));
            log.info("Кнопка создания найдена");
            return element;
        } catch (Exception e) {
            log.error("Ошибка при поиске кнопки создания: {}", e.getMessage());
            return null;
        }
    }

    public void clickDeleteButtonOnLastWishlist() {
        WebElement lastCard = getLastWishlistCard();
        WebElement deleteButton = lastCard.findElement(
                By.xpath(".//button[contains(text(), 'Удалить')]")
        );
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
        log.info("Клик по кнопке 'Удалить' для последнего списка");
    }

    public void confirmDelete() {
        try {
            // Возможно, появляется модальное окно с подтверждением
            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'Подтвердить') or contains(text(), 'Удалить')]")
            ));
            confirmButton.click();
            log.info("Подтверждение удаления");
        } catch (Exception e) {
            log.info("Окно подтверждения не появилось или уже обработано");
        }
    }

}