// src/main/java/com/qea/selenium/handout/OptionsScript.java
package com.qea.selenium.handout;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class OptionsScript {

    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--headless=new");          // no visible window
        options.addArguments("--window-size=1280,800");  // fixed size, same on every machine

        WebDriver driver = new ChromeDriver(options);    // the options are used at start-up
        try {
            driver.get("https://www.saucedemo.com/");
            System.out.println("Title:       " + driver.getTitle());
            System.out.println("Window size: " + driver.manage().window().getSize());
        } finally {
            driver.quit();
        }
    }
}