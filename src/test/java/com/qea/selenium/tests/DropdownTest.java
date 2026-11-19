// src/test/java/com/qea/selenium/tests/DropdownTest.java
package com.qea.selenium.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DropdownTest extends BaseTest {

    @BeforeEach
    void openDropdownPage() {
        driver.get("https://the-internet.herokuapp.com/dropdown");
    }

    private Select dropdown() {
        return new Select(driver.findElement(By.id("dropdown")));
    }

    @Test
    void startsWithThePlaceholder() {
        assertFalse(dropdown().isMultiple());
        assertEquals(3, dropdown().getOptions().size());
        assertEquals("Please select an option", dropdown().getFirstSelectedOption().getText());
    }

    @Test
    void selectByVisibleText() {
        dropdown().selectByVisibleText("Option 1");
        assertEquals("Option 1", dropdown().getFirstSelectedOption().getText());
    }

    @Test
    void selectByValueAndIndex() {
        dropdown().selectByValue("2");
        assertEquals("Option 2", dropdown().getFirstSelectedOption().getText());

        dropdown().selectByIndex(1);
        assertEquals("Option 1", dropdown().getFirstSelectedOption().getText());
    }
}