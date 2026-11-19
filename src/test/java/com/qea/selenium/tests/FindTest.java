// src/test/java/com/qea/selenium/tests/FindTest.java
package com.qea.selenium.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FindTest extends BaseTest {

    @Test
    void findElementReturnsOne() {
        driver.get("https://the-internet.herokuapp.com/tables");
        WebElement heading = driver.findElement(By.tagName("h3")); //first match
        assertEquals("Data Tables", heading.getText());
    }

    @Test
    void findElementsReturnsAll() {
        driver.get("https://the-internet.herokuapp.com/tables");
        List<WebElement> rows = driver.findElements(By.cssSelector("#table1 tbody tr")); //all matches
        assertEquals(4, rows.size());
    }

    @Test
    void scopedSearchReadsOneRow() {
        driver.get("https://the-internet.herokuapp.com/tables");
        WebElement firstRow = driver.findElements(By.cssSelector("#table1 tbody tr")).get(0);
        List<WebElement> cells = firstRow.findElements(By.tagName("td"));

        assertEquals("Smith", cells.get(0).getText());
        assertEquals("John", cells.get(1).getText());
    }

    @Test
    void nothingMatches() {
        driver.get("https://the-internet.herokuapp.com/tables");
        By missing = By.id("does-not-exist");

        assertThrows(NoSuchElementException.class, () -> driver.findElement(missing));
        assertTrue(driver.findElements(missing).isEmpty());
    }

    @Test
    void readCheckboxState() {
        driver.get("https://the-internet.herokuapp.com/checkboxes");
        List<WebElement> boxes = driver.findElements(By.cssSelector("input[type='checkbox']"));

        assertEquals(2, boxes.size());
        assertFalse(boxes.get(0).isSelected());
        assertTrue(boxes.get(1).isSelected());
        assertEquals("checkbox", boxes.get(0).getDomAttribute("type"));
    }
}