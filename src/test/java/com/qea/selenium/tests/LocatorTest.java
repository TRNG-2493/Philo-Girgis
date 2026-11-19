// src/test/java/com/qea/selenium/tests/LocatorTest.java
package com.qea.selenium.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocatorTest extends BaseTest {

    @BeforeEach
    void openLoginPage() {
        driver.get("https://www.saucedemo.com/");
    }

    @Test
    void findByIdAndName() {
        WebElement username = driver.findElement(By.id("user-name"));
        assertEquals("Username", username.getDomAttribute("placeholder"));

        WebElement password = driver.findElement(By.name("password"));
        assertEquals("password", password.getDomAttribute("type"));
    }

    @Test
    void findByClassNameAndCss() {
        WebElement byClass = driver.findElement(By.className("submit-button"));
        assertEquals("Login", byClass.getDomAttribute("value"));

        WebElement byCss = driver.findElement(By.cssSelector("[data-test='login-button']"));
        assertEquals("login-button", byCss.getDomAttribute("id"));
    }

    @Test
    void findByXpath() {
        WebElement heading = driver.findElement(By.xpath("//div[@class='login_password']/h4"));
        assertEquals("Password for all users:", heading.getText());
    }

    @Test
    void findLinksByText() {
        driver.get("https://the-internet.herokuapp.com/");

        WebElement exact = driver.findElement(By.linkText("Form Authentication"));
        assertEquals("Form Authentication", exact.getText());

        WebElement partial = driver.findElement(By.partialLinkText("Dynamic"));
        assertEquals("Dynamic Content", partial.getText());
    }
}