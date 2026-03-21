package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;
import java.util.function.Function;

public class WaitUtils {
    private static final int DEFAULT_TIMEOUT = 10;

    // базовые методы
    public static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public static WebDriverWait getWait(WebDriver driver, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    // ожидание видимости элемента
    public static WebElement waitForVisibility(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibility(WebDriver driver, WebElement element) {
        return getWait(driver).until(ExpectedConditions.visibilityOf(element));
    }

    // ожидание кликабельности
    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, WebElement element) {
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(element));
    }

    // ожидание появления элемента
    public static boolean waitForElementPresent(WebDriver driver, By locator) {
        try {
            getWait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ожидание исчезновения элемента
    public static boolean waitForInvisibility(WebDriver driver, By locator) {
        try {
            return getWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public static boolean waitForInvisibility(WebDriver driver, WebElement element) {
        try {
            return getWait(driver).until(ExpectedConditions.invisibilityOf(element));
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ожидание загрузки страницы
    public static void waitForPageLoad(WebDriver driver) {
        getWait(driver).until(webDriver ->
                ((JavascriptExecutor) webDriver)
                        .executeScript("возвращаем document.readyState")
                        .equals("complete")
        );
    }

    // ожидание текста в элементе
    public static boolean waitForTextPresent(WebDriver driver, By locator, String text) {
        try {
            return getWait(driver).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ожидание количества элементов
    public static Object waitForNumberOfElements(WebDriver driver, By locator, int expectedCount) {
        try {
            return getWait(driver).until(ExpectedConditions.numberOfElementsToBe(locator, expectedCount));
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ожидание с кастомным условием
    public static <V> V waitForCondition(WebDriver driver, Function<WebDriver, V> condition, Duration duration) {
        return getWait(driver).until(condition);
    }

    // ожидание с кастомным таймаутом
    public static <V> V waitForCondition(WebDriver driver, Function<WebDriver, V> condition, int timeoutSeconds) {
        return getWait(driver, timeoutSeconds).until(condition);
    }

    // комбинированное ожидание
    public static void waitForElementAndClick(WebDriver driver, By locator) {
        waitForClickable(driver, locator).click();
    }

    public static void waitForElementAndSendKeys(WebDriver driver, By locator, String text) {
        waitForVisibility(driver, locator).sendKeys(text);
    }
}