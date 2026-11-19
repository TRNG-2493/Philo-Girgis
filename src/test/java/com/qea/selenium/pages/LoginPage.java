// src/test/java/com/qea/selenium/pages/LoginPage.java
package com.qea.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** The Swag Labs login page. */
public class LoginPage {

    private final WebDriver driver;
    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage open() {
        driver.get("https://www.saucedemo.com/");
        return this;
    }

    /** A successful login: the user ends up on the products page. */
    public InventoryPage loginAs(String user, String pass) {
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(loginButton).click();
        return new InventoryPage(driver);
    }

    /** A login that should fail: the user stays on the login page. */
    public LoginPage loginExpectingError(String user, String pass) {
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(loginButton).click();
        return this;
    }

    public String errorMessage() {
        return driver.findElement(errorMessage).getText();
    }
}