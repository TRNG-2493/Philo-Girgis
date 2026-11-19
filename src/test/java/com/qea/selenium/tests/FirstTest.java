// src/test/java/com/qea/selenium/tests/FirstTest.java
package com.qea.selenium.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FirstTest extends BaseTest {

    @Test
    void titleIsSwagLabs() {
        driver.get("https://www.saucedemo.com/");
        assertEquals("Swag Labs", driver.getTitle());
    }

    @Test
    void urlIsTheLoginPage() {
        driver.get("https://www.saucedemo.com/");
        assertEquals("https://www.saucedemo.com/", driver.getCurrentUrl());
    }
}