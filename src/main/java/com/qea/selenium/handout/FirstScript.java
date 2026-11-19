package com.qea.selenium.handout;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class FirstScript {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
        System.out.println("Title: " + driver.getTitle());
        System.out.println("URL:   " + driver.getCurrentUrl());
        driver.quit();
    }
}
