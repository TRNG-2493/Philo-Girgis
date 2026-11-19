package com.revature.qea.steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Parent of every step class and of Hooks: holds the one browser they all share. */
public class BaseSteps {

    protected static WebDriver driver;
    protected static WebDriverWait wait;
}