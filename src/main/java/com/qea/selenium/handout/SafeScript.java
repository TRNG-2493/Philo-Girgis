package com.qea.selenium.handout;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SafeScript {

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();     // Selenium Manager provides chromedriver
        try {
            driver.get("https://www.saucedemo.com/");
            System.out.println("Title: " + driver.getTitle());

            driver.get("https://the-internet.herokuapp.com/login");
            System.out.println("Title: " + driver.getTitle());
        } finally {
            driver.quit();                         // always runs, even after an exception
        }
    }
}
