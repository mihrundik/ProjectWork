package pages.assertions;

import pages.AddGiftPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddGiftPageAssertions {

    private final AddGiftPage page;

    public AddGiftPageAssertions(AddGiftPage page) {
        this.page = page;
    }

    /**
     * Проверяет, что все поля формы содержат ожидаемые значения
     */
    public void verifyGiftFormData(String expectedName,
                                   String expectedDescription,
                                   String expectedProductUrl,
                                   String expectedPrice,
                                   String expectedImageUrl) {
        assertEquals(expectedName, page.getGiftNameField().getAttribute("value"),
                "Поле Название подарка не содержит ожидаемого значения");
        assertEquals(expectedDescription, page.getGiftDescriptionField().getAttribute("value"),
                "Поле Описание подарка не содержит ожидаемого значения");
        assertEquals(expectedProductUrl, page.giftUrlProdact().getAttribute("value"),
                "Поле URL продукта не содержит ожидаемого значения");
        assertEquals(expectedPrice, page.giftPriceProdact().getAttribute("value"),
                "Поле Цена не содержит ожидаемого значения");
        assertEquals(expectedImageUrl, page.giftUrlImage().getAttribute("value"),
                "Поле URL картинки не содержит ожидаемого значения");
    }

    /**
     * Проверяет, что модальное окно отображается
     */
    public void verifyModalDisplayed() {
        assertTrue(page.isModalDisplayed(), "Модальное окно не отображено");
    }

    /**
     * Проверяет, что модальное окно закрылось
     */
    public void verifyModalClosed() {
        assertTrue(page.waitForModalToDisappear(), "Модальное окно не закрылось");
    }
}