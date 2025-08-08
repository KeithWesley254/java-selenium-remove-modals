package com.example.app;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GenUtils {

    /**
     * Hides a modal popup using JavaScript, even if it has no close button.
     * @param driver The WebDriver instance.
     * @param modal The WebElement representing the modal container.
     */
    public static void hideModalUsingJS(WebDriver driver, WebElement modal) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("""
                arguments[0].style.display = 'none';
                document.body.style.overflow = 'auto';
                let backdrop = document.querySelector('[class*="Backdrop"]');
                if (backdrop) backdrop.style.display = 'none';
            """, modal);
            System.out.println("Modal was hidden via JS.");
        } catch (Exception ex) {
            System.out.println("Failed to hide modal: " + ex.getMessage());
        }
    }

    /**
     * Types text into a field one character at a time, simulating a human typing.
     * @param input The WebElement input field.
     * @param text The text to type.
     */
    public static void typeSlowly(WebElement input, String text) throws InterruptedException {
        input.clear();
        for (char c : text.toCharArray()) {
            input.sendKeys(String.valueOf(c));
            Thread.sleep(75); // simulate real typing
        }
    }

    /**
     * Extracts only numbers from a string and converts to int.
     * @param text The input string containing numbers.
     * @return The integer value.
     */
    public static int extractNumberFromText(String text) {
        String numberString = text.replaceAll("[^\\d,]", ""); // Keep only digits and commas
        numberString = numberString.replace(",", ""); // Remove commas
        return Integer.parseInt(numberString);
    }

    /**
     * Normalizes all dash types into a single hyphen with spaces.
     * @param text The input string.
     * @return The normalized string.
     */
    public static String normalizeDashes(String text) {
        if (text == null) return "";
        // Replace all dash types with a single hyphen, normalize spaces around it
        return text
            .replaceAll("[‐-‒–—―]", "-")
            .replaceAll("\\s*-\\s*", " - ")
            .trim();
    }

    /**
     * Waits for an enabled submit button and clicks it (normal or JS fallback).
     * @param driver The WebDriver instance.
     * @param waitTimeoutSeconds Timeout in seconds to wait for the button.
     * @param cssSelector The CSS selector of the submit button.
     */
    public static void clickEnabledSubmitButton(WebDriver driver, int waitTimeoutSeconds, String cssSelector) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeoutSeconds));

            WebElement submitButton = wait.until(d -> {
                WebElement btn = d.findElement(By.cssSelector(cssSelector));
                return btn.isEnabled() ? btn : null;
            });

            // Scroll into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);

            // Click (normal or JS fallback)
            try {
                submitButton.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
            }

            System.out.println("Clicked submit button.");
        } catch (Exception e) {
            System.out.println("Failed to click submit button: " + e.getMessage());
        }
    }
}
