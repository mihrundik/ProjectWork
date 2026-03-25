package pages;

import config.EnvConfig;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyWishlistsPage {

    public Logger log = LogManager.getLogger(MyWishlistsPage.class);

    public final WebDriver driver;
    public final WebDriverWait wait;

    public static final int DEFAULT_TIMEOUT_SECONDS = 10;

    @FindBy(css = "h2")
    private WebElement pageTitle;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement addNewListButton;

    @FindBy(css = "div.g-4.row")
    private WebElement wishlistContainer;

    @FindBy(css = "input.form-control[type='text'][required]")
    private WebElement nameNewWL;

    @FindBy(css = "textarea.form-control")
    private WebElement descriptionNewWL;

    @FindBy(css = "button.btn-close")
    private WebElement closeButton;

    @FindBy(xpath = "//button[contains(text(), 'Отмена')]")
    private WebElement cancelButton;

    @FindBy(xpath = "//button[@type='submit' and contains(text(), 'Создать')]")
    private WebElement submitButton;

    /**
     * Конструктор страницы со списком вишлистов.
     */
    public MyWishlistsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    /**
     * Открывает страницу со списком вишлистов.
     */
    public void open() {
        driver.get(EnvConfig.getUrl() + "/wishlists");
        waitForPageToLoad();
    }

    /**
     * Ожидает загрузки страницы (появление заголовка).
     */
    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        log.info("Страница Мои списки загружена");
    }

    /**
     * Нажимает кнопку "Добавить список".
     */
    public void clickAddNewList() {
        wait.until(ExpectedConditions.elementToBeClickable(addNewListButton)).click();
        log.info("Клик по кнопке 'Добавить список");
    }

    /**
     * Возвращает список уникальных карточек вишлистов.
     * Фильтрует дубликаты по заголовку.
     */
    public List<WebElement> getWishlistCards() {
        try {
            wait.until(ExpectedConditions.visibilityOf(wishlistContainer));

            // Находим все карточки с классом 'card'
            List<WebElement> allCards = wishlistContainer.findElements(By.xpath(".//div[contains(@class, 'card')]"));

            // Используем Map для хранения уникальных вишлистов по их заголовку
            Map<String, WebElement> uniqueWishlists = new HashMap<>();

            for (WebElement card : allCards) {
                // Проверяем, есть ли кнопка "Просмотр"
                List<WebElement> viewButtons = card.findElements(By.xpath(".//button[contains(text(), 'Просмотр')]"));

                if (!viewButtons.isEmpty()) {
                    // Получаем заголовок вишлиста
                    String title = "";
                    try {
                        title = card.findElement(By.xpath(".//div[contains(@class, 'card-title')]")).getText();
                    } catch (Exception e) {
                        // Если нет заголовка, используем текст карточки
                        title = card.getText();
                    }

                    // Сохраняем только первый экземпляр каждого вишлиста
                    if (!uniqueWishlists.containsKey(title)) {
                        uniqueWishlists.put(title, card);
                        log.debug("Добавлен уникальный вишлист: {}", title);
                    }
                }
            }

            return new ArrayList<>(uniqueWishlists.values());

        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Возвращает количество вишлистов на странице.
     */
    public int getWishlistCount() {
        return getWishlistCards().size();
    }

    /**
     * Проверяет, существует ли хотя бы один вишлист.
     */
    public boolean hasWishlists() {
        return getWishlistCount() > 0;
    }

    /**
     * Возвращает индекс последней карточки вишлиста.
     */
    private int getLastIndex() {
        return getWishlistCount() - 1;
    }

    /**
     * Возвращает последнюю карточку вишлиста.
     */
    public WebElement getLastWishlistCard() {
        List<WebElement> cards = getWishlistCards();
        if (cards.isEmpty()) {
            throw new IllegalStateException("Нет ни одной карточки. Список пуст!");
        }
        return cards.get(getLastIndex());
    }

    /**
     * Нажимает кнопку "Просмотр" на последнем вишлисте.
     */
    public void clickViewButtonOnLastWishlist() {
        WebElement lastCard = getLastWishlistCard();
        WebElement viewButton = lastCard.findElement(
                By.xpath(".//button[contains(@class, 'btn-primary') and contains(text(), 'Просмотр')]")
        );
        wait.until(ExpectedConditions.elementToBeClickable(viewButton)).click();
        log.info("Клик по кнопке 'Просмотр' для последнего списка");
    }

    /**
     * Возвращает заголовок последнего вишлиста.
     */
    public String getLastWishlistTitle() {
        WebElement lastCard = getLastWishlistCard();
        WebElement titleElement = lastCard.findElement(
                By.xpath(".//div[contains(@class, 'card-title') and contains(@class, 'h5')]")
        );
        return titleElement.getText();
    }

    /**
     * Возвращает описание последнего вишлиста.
     */
    public String getLastWishlistDescription() {
        WebElement lastCard = getLastWishlistCard();
        WebElement descElement = lastCard.findElement(By.xpath(".//p[@class='card-text']"));
        return descElement.getText();
    }

    /**
     * Возвращает текст с количеством подарков в последнем вишлисте.
     */
    public String getLastWishlistGiftCountText() {
        WebElement lastCard = getLastWishlistCard();
        WebElement countElement = lastCard.findElement(By.xpath(".//small[@class='text-muted']"));
        return countElement.getText();
    }

    /**
     * Возвращает список всех уникальных карточек вишлистов.
     */
    public List<WebElement> getAllWishlistCards() {
        return getWishlistCards();
    }

    /**
     * Ожидает появления формы создания вишлиста.
     */
    public void waitForCreateFormToAppear() {
        wait.until(ExpectedConditions.visibilityOf(nameNewWL));
        log.info("Форма создания вишлиста появилась");
    }

    /**
     * Ожидает закрытия формы создания вишлиста.
     */
    public void waitForCreateFormToDisappear() {
        try {
            // Ждем, пока поле названия станет невидимым
            wait.until(ExpectedConditions.invisibilityOf(nameNewWL));
            log.info("Форма создания вишлиста закрылась");
        } catch (TimeoutException e) {
            // Проверяем альтернативные локаторы
            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                        By.xpath("//form[.//input[@placeholder='Введите название']]")));
                log.info("Форма создания вишлиста закрылась (альтернативный локатор)");
            } catch (TimeoutException e2) {
                // Если форма все еще видима, логируем ошибку
                log.warn("Форма создания вишлиста не закрылась за {} секунд", DEFAULT_TIMEOUT_SECONDS);
                throw e2;
            }
        }
    }

    /**
     * Заполняет форму создания нового вишлиста.
     */
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

    /**
     * Нажимает кнопку "Отмена" в форме создания вишлиста.
     */
    public void clickCancelButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
            button.click();
            log.info("Клик по кнопке 'Отмена'");
        } catch (Exception e) {
            log.error("Не удалось нажать кнопку 'Отмена': {}", e.getMessage());
            // Пробуем альтернативную кнопку закрытия
            try {
                WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(closeButton));
                closeBtn.click();
                log.info("Клик по кнопке закрытия (крестик) как альтернатива");
            } catch (Exception e2) {
                throw new RuntimeException("Не удалось закрыть форму создания", e2);
            }
        }
    }

    /**
     * Нажимает кнопку закрытия (крестик) в форме создания вишлиста.
     */
    public void clickCloseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
        log.info("Клик по кнопке закрытия (крестик)");
    }

    /**
     * Нажимает кнопку "Создать" в форме создания вишлиста.
     */
    public void clickSubmitButton() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        log.info("Клик по кнопке 'Создать'");
    }

    /**
     * Возвращает поле ввода названия вишлиста.
     */
    public WebElement getNameNewWL() {
        try {
            // Проверяем, есть ли модальное окно
            List<WebElement> modals = driver.findElements(By.xpath("/html/body/div[3]"));

            if (modals.isEmpty()) {
                log.error("Модальное окно не найдено!");
                return null;
            }

            // Ищем поле ввода
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input.form-control[type='text']")
            ));
            return element;
        } catch (TimeoutException e) {
            log.error("Таймаут при поиске поля названия: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка при поиске поля названия: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Возвращает поле ввода описания вишлиста.
     */
    public WebElement getDescriptionNewWL() {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("textarea.form-control")
            ));
            return element;
        } catch (Exception e) {
            log.error("Ошибка при поиске поля описания: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Переинициализирует элементы модального окна.
     * Поля инициализируются один раз при создании объекта страницы,
     * но если элемент появляется позже (в модальном окне), то поле может быть null.
     * Поэтому требуется повторная инициализация.
     */
    public void initModalElements() {
        PageFactory.initElements(driver, this);
        log.info("Элементы модального окна переинициализированы");
    }

    /**
     * Закрывает форму создания вишлиста при зависании.
     */
    public void closeCreateForm() {
        try {
            // Пробуем найти кнопку "Отмена" или "Закрыть"
            WebElement cancelButton = driver.findElement(
                    By.xpath("//button[contains(text(), 'Отмена')] | //button[contains(text(), 'Закрыть')] | //button[contains(text(), 'Cancel')]"));
            cancelButton.click();
            log.info("Форма закрыта через кнопку отмены");
        } catch (Exception e) {
            // Если нет кнопки, пробуем нажать Escape
            driver.findElement(By.tagName("body")).sendKeys(org.openqa.selenium.Keys.ESCAPE);
            log.info("Форма закрыта через клавишу Escape");
        }

        // Ждем, пока форма действительно закроется
        waitForCreateFormToDisappear();
    }

    /**
     * Проверяет, что вишлисты существуют
     */
    public void verifyWishlistsExist() {
        assertTrue(hasWishlists(), "Должен быть хотя бы один список желаний");
    }

    /**
     * Проверяет, что кнопка просмотра активна для всех вишлистов
     */
    public void verifyAllViewButtonsClickable() {
        List<WebElement> wishlistCards = getAllWishlistCards();
        for (WebElement card : wishlistCards) {
            String title = card.findElement(By.xpath(".//div[contains(@class, 'card-title')]")).getText();
            WebElement viewButton = card.findElement(By.xpath(".//button[contains(text(), 'Просмотр')]"));

            assertTrue(viewButton.isEnabled(),
                    String.format("Кнопка 'Просмотр' для вишлиста '%s' должна быть активна", title));
        }
    }

    /**
     * Проверяет, что вишлист был удален
     */
    public void verifyWishlistDeleted(int initialCount) {
        waitForPageToLoad();
        int newCount = getWishlistCount();
        assertEquals(initialCount - 1, newCount,
                String.format("Количество вишлистов должно уменьшиться на 1. Было: %d, стало: %d", initialCount, newCount));
    }

    /**
     * Проверяет изменение количества вишлистов
     */
    public void verifyWishlistCountChanged(int expectedCount, String operation) {
        int actualCount = getWishlistCount();
        assertEquals(expectedCount, actualCount,
                String.format("Количество списков %s: ожидалось %d, фактически %d",
                        operation, expectedCount, actualCount));
        log.info("Количество списков {}: {} (ожидалось {})", operation, actualCount, expectedCount);
    }

    /**
     * Проверяет, что поля формы создания отображаются
     */
    public void verifyCreateFormFieldsDisplayed() {
        initModalElements();
        WebElement nameField = getNameNewWL();
        WebElement descField = getDescriptionNewWL();
        assertTrue(nameField != null && nameField.isDisplayed(), "Поле названия должно отображаться");
        assertTrue(descField != null && descField.isDisplayed(), "Поле описания должно отображаться");
    }

    /**
     * Проверяет, что количество кнопок "Просмотр" равно количеству вишлистов
     */
    public void verifyNumberOfViewButtonsEqualsWishlistCount() {
        int wishlistCount = getWishlistCount();
        List<WebElement> viewButtons = driver.findElements(By.xpath("//button[contains(text(), 'Просмотр')]"));
        int actualViewButtonsCount = viewButtons.size();

        assertEquals(wishlistCount, actualViewButtonsCount,
                "Количество вишлистов должно равняться количеству кнопок 'Просмотр'");
    }

    /**
     * Проверяет данные нового вишлиста
     */
    public void verifyNewWishlistData(String expectedTitle, String expectedDescription, String expectedGiftCount) {
        String actualTitle = getLastWishlistTitle();
        String actualDescription = getLastWishlistDescription();
        String actualGiftCount = getLastWishlistGiftCountText();

        assertTrue(actualTitle != null, "Название не должно быть пустым");
        assertTrue(!actualTitle.isEmpty(), "Название не должно быть пустой строкой");
        assertEquals(expectedTitle, actualTitle, "Название не соответствует созданному");
        assertEquals(expectedDescription, actualDescription, "Описание не соответствует созданному");

        assertTrue(actualGiftCount.contains(expectedGiftCount),
                "В новом списке должно быть " + expectedGiftCount + " подарков, получено: " + actualGiftCount);
    }

    /**
     * Проверяет, что форма создания вишлиста не видима
     */
    public void verifyCreateFormNotVisible() {
        // Небольшая задержка для закрытия анимации
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isVisible = isCreateFormVisible();
        assertTrue(!isVisible, "Форма создания не должна быть видима. Текущее состояние: " + isVisible);
        log.info("Форма создания вишлиста не видима");
    }

    /**
     * Проверяет, что клик по кнопке "Просмотр" последнего вишлиста выполнен успешно
     */
    public void verifyViewButtonClickable() {
        clickViewButtonOnLastWishlist();
        // Если дошли до этого места без исключений - клик выполнен успешно
        log.info("Клик по кнопке 'Просмотр' успешно выполнен");
    }

    /**
     * Проверяет, что форма создания вишлиста видима
     */
    public void verifyCreateFormVisible() {
        // Небольшая задержка для анимации
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isVisible = isCreateFormVisible();
        assertTrue(isVisible, "Форма создания должна быть видима");
        log.info("Форма создания вишлиста видима");
    }

    /**
     * Проверяет, отображается ли форма создания вишлиста.
     */
    public boolean isCreateFormVisible() {
        try {
            // Пробуем найти форму по разным локаторам
            List<WebElement> forms = driver.findElements(By.cssSelector("form.create-wishlist-form"));
            if (!forms.isEmpty() && forms.get(0).isDisplayed()) {
                return true;
            }

            forms = driver.findElements(By.xpath("//form[.//input[@placeholder='Введите название']]"));
            if (!forms.isEmpty() && forms.get(0).isDisplayed()) {
                return true;
            }

            // Проверяем видимость поля названия как индикатор открытой формы
            try {
                return nameNewWL.isDisplayed();
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            log.debug("Форма создания не найдена или не видима: {}", e.getMessage());
            return false;
        }
    }

}
