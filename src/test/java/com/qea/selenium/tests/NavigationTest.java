// src/test/java/com/qea/selenium/tests/NavigationTest.java
package com.qea.selenium.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NavigationTest extends BaseTest {

    static final String HOME  = "https://the-internet.herokuapp.com/";
    static final String LOGIN = "https://the-internet.herokuapp.com/login";

    @Test
    void getOpensThePage() {
        driver.get(HOME);
        assertEquals(HOME, driver.getCurrentUrl());
        assertEquals("The Internet", driver.getTitle());
    }

    @Test
    void backAndForwardMoveThroughHistory() {
        driver.get(HOME);
        driver.navigate().to(LOGIN);
        assertEquals(LOGIN, driver.getCurrentUrl());

        driver.navigate().back();
        assertEquals(HOME, driver.getCurrentUrl());

        driver.navigate().forward();
        assertEquals(LOGIN, driver.getCurrentUrl());
    }

    @Test
    void refreshStaysOnTheSamePage() {
        driver.get(LOGIN);
        driver.navigate().refresh();
        assertEquals(LOGIN, driver.getCurrentUrl());
    }

    @Test
    void pageSourceContainsTheHeading() {
        driver.get(LOGIN);
        assertTrue(driver.getPageSource().contains("Login Page"));
    }
}