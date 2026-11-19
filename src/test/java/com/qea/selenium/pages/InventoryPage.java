// src/test/java/com/qea/selenium/pages/InventoryPage.java
package com.qea.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/** The Swag Labs products page. */
public class InventoryPage {

    private final WebDriver driver;
    private final By title = By.cssSelector("[data-test='title']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public String title() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(title))
                .getText();
    }

    public InventoryPage addToCart(String buttonId) {
        driver.findElement(By.id(buttonId)).click();
        return this;
    }

    /** 0 when the badge is not shown: an empty cart is a valid answer. */
    public int cartCount() {
        List<WebElement> badge = driver.findElements(cartBadge);
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText());
    }
}