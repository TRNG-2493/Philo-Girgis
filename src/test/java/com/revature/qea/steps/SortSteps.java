package com.revature.qea.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Step definitions for sorting.feature. The login steps come from LoginSteps. */
public class SortSteps extends BaseSteps {

    @When("I sort the products by {string}")
    public void iSortTheProductsBy(String order) {
        WebElement sortMenu = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='product-sort-container']")));
        new Select(sortMenu).selectByVisibleText(order);
    }

    @Then("the first product should be {string}")
    public void theFirstProductShouldBe(String expectedName) {
        // findElement returns the FIRST matching element: the first product on the page
        String firstName = driver.findElement(By.cssSelector("[data-test='inventory-item-name']")).getText();
        assertEquals(expectedName, firstName);
    }
}