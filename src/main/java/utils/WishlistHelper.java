package utils;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.MyWishlistsPage;

public class WishlistHelper {

    private static final Logger log = LoggerFactory.getLogger(WishlistHelper.class);

    private final MyWishlistsPage wishlistsPage;

    public WishlistHelper(WebDriver driver) {
        this.wishlistsPage = new MyWishlistsPage(driver);
    }

    // наличие хотя бы одного вишлиста на странице
    public String ensureWishlistExists() {
        return ensureWishlistExists(null, null);
    }

    // наличие хотя бы одного вишлиста на странице с указанными параметрами
    public String ensureWishlistExists(String wishlistName, String wishlistDescription) {
        wishlistsPage.open();

        if (wishlistsPage.hasWishlists()) {
            log.info("Вишлисты уже существуют. Использую существующие.");
            return wishlistsPage.getLastWishlistTitle();
        }

        log.info("Вишлисты отсутствуют. Создаю новый...");
        return createWishlist(wishlistName, wishlistDescription);
    }

    // создает новый вишлист с автоматически сгенерированным названием
    public String createWishlist() {
        return createWishlist(null, null);
    }

    // создает новый вишлист с указанными параметрами
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

    // генерирует уникальное название для вишлиста
    private String generateWishlistName() {
        return "Автотест-вишлист " + System.currentTimeMillis();
    }

    // генерирует описание для вишлиста по умолчанию
    private String generateWishlistDescription() {
        return "Этот вишлист создан для автоматического теста";
    }

    public MyWishlistsPage getWishlistsPage() {
        return wishlistsPage;
    }
}