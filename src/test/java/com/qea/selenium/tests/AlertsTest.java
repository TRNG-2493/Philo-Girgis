// src/test/java/com/qea/selenium/tests/AlertsTest.java
package com.qea.selenium.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlertsTest extends BaseTest {

    @BeforeEach
    void openPage() {
        driver.get("https://the-internet.herokuapp.com/javascript_alerts");
    }

    private void clickButton(String text) {
        driver.findElement(By.xpath("//button[text()='" + text + "']")).click();
    }

    @Test
    void acceptAnAlert() {
        clickButton("Click for JS Alert");

        Alert alert = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.alertIsPresent());
        assertEquals("I am a JS Alert", alert.getText());
        alert.accept();

        assertEquals("You successfully clicked an alert", driver.findElement(By.id("result")).getText());
    }

    @Test
    void dismissAConfirm() {
        clickButton("Click for JS Confirm");

        Alert confirm = driver.switchTo().alert();
        assertEquals("I am a JS Confirm", confirm.getText());
        confirm.dismiss();

        assertEquals("You clicked: Cancel", driver.findElement(By.id("result")).getText());
    }

    @Test
    void answerAPrompt() {
        clickButton("Click for JS Prompt");

        Alert prompt = driver.switchTo().alert();
        prompt.sendKeys("Selenium");
        prompt.accept();

        assertEquals("You entered: Selenium", driver.findElement(By.id("result")).getText());
    }
}