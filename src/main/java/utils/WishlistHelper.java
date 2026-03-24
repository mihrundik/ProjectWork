package utils;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.MyWishlistsPage;

/**
 * Вспомогательный класс для работы с вишлистами в тестах.
 * Предоставляет методы для обеспечения наличия вишлистов и их создания.
 */
public class WishlistHelper {

    private static final Logger log = LoggerFactory.getLogger(WishlistHelper.class);

    private final MyWishlistsPage wishlistsPage;

    public WishlistHelper(WebDriver driver) {
        this.wishlistsPage = new MyWishlistsPage(driver);
    }

    /**
     * Гарантирует наличие хотя бы одного вишлиста на странице.
     * Если вишлисты отсутствуют, создает новый с автоматически сгенерированным названием.
     *
     * @return название созданного вишлиста (если был создан) или null (если вишлисты уже были)
     */
    public String ensureWishlistExists() {
        return ensureWishlistExists(null, null);
    }

    /**
     * Гарантирует наличие хотя бы одного вишлиста на странице с указанными параметрами.
     * Если вишлисты отсутствуют, создает новый с переданными названием и описанием.
     *
     * @param wishlistName название вишлиста (если null, будет сгенерировано автоматически)
     * @param wishlistDescription описание вишлиста (если null, будет использовано описание по умолчанию)
     * @return название вишлиста (существующего или созданного)
     */
    public String ensureWishlistExists(String wishlistName, String wishlistDescription) {
        wishlistsPage.open();

        if (wishlistsPage.hasWishlists()) {
            log.info("Вишлисты уже существуют. Использую существующие.");
            return wishlistsPage.getLastWishlistTitle();
        }

        log.info("Вишлисты отсутствуют. Создаю новый...");
        return createWishlist(wishlistName, wishlistDescription);
    }

    /**
     * Создает новый вишлист с автоматически сгенерированным названием.
     *
     * @return название созданного вишлиста
     */
    public String createWishlist() {
        return createWishlist(null, null);
    }

    /**
     * Создает новый вишлист с указанными параметрами.
     *
     * @param wishlistName название вишлиста (если null, будет сгенерировано автоматически)
     * @param wishlistDescription описание вишлиста (если null, будет использовано описание по умолчанию)
     * @return название созданного вишлиста
     */
    public String createWishlist(String wishlistName, String wishlistDescription) {
        String name = wishlistName != null ? wishlistName : generateWishlistName();
        String description = wishlistDescription != null ? wishlistDescription : generateWishlistDescription();

        wishlistsPage.clickAddNewList();
        wishlistsPage.waitForCreateFormToAppear();

        wishlistsPage.fillCreateForm(name, description);
        wishlistsPage.clickSubmitButton();

        wishlistsPage.waitForCreateFormToDisappear();

        log.info("Создан новый вишлист: {} с описанием: {}", name, description);
        return name;
    }

    /**
     * Генерирует уникальное название для вишлиста.
     *
     * @return сгенерированное название
     */
    private String generateWishlistName() {
        return "Автотест-вишлист " + System.currentTimeMillis();
    }

    /**
     * Генерирует описание для вишлиста по умолчанию.
     *
     * @return описание по умолчанию
     */
    private String generateWishlistDescription() {
        return "Этот вишлист создан для автоматического теста";
    }

    /**
     * Возвращает объект страницы вишлистов для дальнейшей работы.
     *
     * @return объект MyWishlistsPage
     */
    public MyWishlistsPage getWishlistsPage() {
        return wishlistsPage;
    }
}