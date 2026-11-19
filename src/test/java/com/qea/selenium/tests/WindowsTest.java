// src/test/java/com/qea/selenium/tests/WindowsTest.java
package com.qea.selenium.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WindowsTest extends BaseTest {

    @Test
    void switchToANewWindowAndBack() {
        driver.get("https://the-internet.herokuapp.com/windows");
        String original = driver.getWindowHandle();

        driver.findElement(By.linkText("Click Here")).click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.numberOfWindowsToBe(2));

        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(original)) {
                driver.switchTo().window(handle);
            }
        }
        assertEquals("New Window", driver.getTitle());

        driver.close();
        driver.switchTo().window(original);
        assertEquals("The Internet", driver.getTitle());
    }

    @Test
    void readTextInsideNestedFrames() {
        driver.get("https://the-internet.herokuapp.com/nested_frames");
        assertTrue(driver.findElements(By.id("content")).isEmpty());

        driver.switchTo().frame("frame-top");
        driver.switchTo().frame("frame-middle");
        assertEquals("MIDDLE", driver.findElement(By.id("content")).getText());

        driver.switchTo().defaultContent();
    }
}