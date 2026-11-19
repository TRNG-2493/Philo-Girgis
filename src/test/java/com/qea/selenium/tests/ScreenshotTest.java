// src/test/java/com/qea/selenium/tests/ScreenshotTest.java
package com.qea.selenium.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ScreenshotTest extends BaseTest {

    @Test
    void saveAPageScreenshot() throws IOException {
        driver.get("https://www.saucedemo.com/");

        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path target = Path.of("screenshots", "login-page.png");
        Files.createDirectories(target.getParent());
        Files.copy(source.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

        assertTrue(Files.exists(target));
    }

    @Test
    void saveAnElementScreenshot() throws IOException {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("login-button")).click();
        WebElement error = driver.findElement(By.cssSelector("[data-test='error']"));

        File source = error.getScreenshotAs(OutputType.FILE);
        Path target = Path.of("screenshots", "login-error.png");
        Files.createDirectories(target.getParent());
        Files.copy(source.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

        assertTrue(Files.exists(target));
    }
}