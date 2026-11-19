// src/test/java/com/qea/selenium/tests/WaitTest.java
package com.qea.selenium.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WaitTest extends BaseTest {

    @Test
    void withoutAWaitTheResultIsMissing() {
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/2");
        driver.findElement(By.cssSelector("#start button")).click();

        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("finish")));
    }

    @Test
    void explicitWaitSeesTheResult() {
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/2");
        driver.findElement(By.cssSelector("#start button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement finish = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));

        assertEquals("Hello World!", finish.getText());
    }

    @Test
    void presenceIsNotVisibility() {
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");
        driver.findElement(By.cssSelector("#start button")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement present = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("finish")));
        assertFalse(present.isDisplayed());

        WebElement visible = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
        assertEquals("Hello World!", visible.getText());
    }

    @Test
    void implicitWaitAlsoFindsIt() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/2");
        driver.findElement(By.cssSelector("#start button")).click();

        assertEquals("Hello World!", driver.findElement(By.id("finish")).getText());
    }
}