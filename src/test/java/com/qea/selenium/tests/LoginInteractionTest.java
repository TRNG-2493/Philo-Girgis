// src/test/java/com/qea/selenium/tests/LoginInteractionTest.java
package com.qea.selenium.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginInteractionTest extends BaseTest {

    @BeforeEach
    void openLoginPage() {
        driver.get("https://the-internet.herokuapp.com/login");
    }

    @Test
    void validLoginOpensSecureArea() {
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertEquals("https://the-internet.herokuapp.com/secure", driver.getCurrentUrl());
        String message = driver.findElement(By.id("flash")).getText();
        assertTrue(message.contains("You logged into a secure area!"));
    }

    @Test
    void wrongPasswordShowsError() {
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("wrong");
        driver.findElement(By.cssSelector("button[type='submit']")).sendKeys(Keys.ENTER);

        String message = driver.findElement(By.id("flash")).getText();
        assertTrue(message.contains("Your password is invalid!"));
    }

    @Test
    void sendKeysAddsAndClearEmpties() {
        WebElement username = driver.findElement(By.id("username"));

        username.sendKeys("tom");
        username.sendKeys("smith");
        assertEquals("tomsmith", username.getDomProperty("value"));

        username.clear();
        assertEquals("", username.getDomProperty("value"));
    }

    @Test
    void clickTicksACheckbox() {
        driver.get("https://the-internet.herokuapp.com/checkboxes");
        WebElement first = driver.findElements(By.cssSelector("input[type='checkbox']")).get(0);

        assertFalse(first.isSelected());
        first.click();
        assertTrue(first.isSelected());
    }
}