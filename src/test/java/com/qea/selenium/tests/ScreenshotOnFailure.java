// src/test/java/com/qea/selenium/tests/ScreenshotOnFailure.java
package com.qea.selenium.tests;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Saves a screenshot named after the test whenever a test fails, before the browser quits. */
public class ScreenshotOnFailure implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (context.getExecutionException().isPresent()) {
            BaseTest test = (BaseTest) context.getRequiredTestInstance();
            File source = ((TakesScreenshot) test.driver).getScreenshotAs(OutputType.FILE);
            Path target = Path.of("screenshots", context.getRequiredTestMethod().getName() + ".png");
            Files.createDirectories(target.getParent());
            Files.copy(source.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved: " + target);
        }
    }
}