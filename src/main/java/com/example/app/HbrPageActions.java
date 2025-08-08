package com.example.app;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public class HbrPageActions {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public HbrPageActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void closeInitialModalIfPresent() {
        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement modal = longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div[class*='Modal-module_container']")
            ));

            try {
                WebElement closeBtn = modal.findElement(By.cssSelector("button[aria-label='Close']"));
                if (closeBtn.isDisplayed() && closeBtn.isEnabled()) {
                    closeBtn.click();
                    System.out.println("Clicked modal close button.");
                } else {
                    hideModalUsingJS(modal);
                }
            } catch (NoSuchElementException ce) {
                hideModalUsingJS(modal);
            }

            Thread.sleep(2000); // wait for modal to disappear
        } catch (TimeoutException e) {
            System.out.println("No modal appeared.");
        } catch (InterruptedException ignored) {}
    }

    private void hideModalUsingJS(WebElement modal) {
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

    public void checkHomepageTitle(String expectedPageTitle) {
        String actualPageTitle = driver.getTitle();

        // Make all dashes similar
        String normalizedActual = actualPageTitle
                .replace("\u2013", "-")
                .replace("\u2014", "-") 
                .replaceAll("\\s*-\\s*", " - ") 
                .trim();

        String normalizedExpected = expectedPageTitle
                .replace("\u2013", "-")
                .replace("\u2014", "-")
                .replaceAll("\\s*-\\s*", " - ")
                .trim();

        Assertions.assertEquals(normalizedExpected, normalizedActual, 
            "Page title mismatch before search.");

        System.out.println("Verified page title before search: " + actualPageTitle);
    }


    public void signInWithFakeCredentialsAndRetry() {
        try {
            // Open the Sign In form
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@id='sign-in-button-header']")
            ));
            signInButton.click();

            // Wait for input fields
            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[id='text-input:r0:']")
            ));
            WebElement passwordInput = driver.findElement(By.cssSelector("input[id='text-input:r1:']"));

            // First attempt
            typeSlowly(emailInput, "johnConstantine@example.com");
            typeSlowly(passwordInput, "johnConstantine123");
            clickEnabledSubmitButton();

            // Wait for and print error message
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@class='AlertMessage-module_error-message__nmgov']")
            ));
            System.out.println("Sign In Error Message: " + errorMessage.getText());

            // Retry with different email
            Thread.sleep(3000); // Simulate user reading error

            emailInput.clear();
            typeSlowly(emailInput, "freddyKruger@example.com");
            clickEnabledSubmitButton();

            System.out.println("Retried Sign In with second email.");

            Thread.sleep(3000); //Checking result

        } catch (TimeoutException te) {
            System.out.println("Timed out waiting for Sign In form or error message.");
        } catch (Exception e) {
            System.out.println("Failed during Sign In test: " + e.getMessage());
        }
    }



    public void clickSubscribeAndVerifyRedirectInNewTab() {
        try {
            // String originalWindow = driver.getWindowHandle();
            Set<String> existingWindows = driver.getWindowHandles();

            WebElement subscribe = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".Anchor-module_anchor__uhpem.Header-module_subscribe-button__mGvj9")
            ));
            subscribe.click();

            // Wait for a new tab to open
            WebDriverWait newTabWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            newTabWait.until(driver -> driver.getWindowHandles().size() > existingWindows.size());

            // Get the handle for the new window
            Set<String> updatedWindows = driver.getWindowHandles();
            updatedWindows.removeAll(existingWindows);
            String newWindow = updatedWindows.iterator().next();

            // Switch to new tab
            driver.switchTo().window(newWindow);
            System.out.println("Redirected to new tab URL: " + driver.getCurrentUrl());

            // Wait for the new page to fully load (adjust selector if needed)
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.tagName("body"))
            );

            // Close new tab and return to original
            // driver.close();
            // driver.switchTo().window(originalWindow);

        } catch (TimeoutException e) {
            System.out.println("New tab did not open in time.");
        } catch (Exception e) {
            System.out.println("Error handling Subscribe redirect in new tab: " + e.getMessage());
        }
    }

    public void extract3Titles() {

        WebElement featuredTitle = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".Featured_title__MsPRd")
        ));
        WebElement firstTopTitle = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//h2[contains(@class, 'ForYouCard_title')])[1]")
        ));
        WebElement secondSupportTitle = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//h2[contains(@class, 'ForYouCard_title')])[2]")
        ));

        boolean flag = secondSupportTitle.isDisplayed();

        if (flag) {
            System.out.println("The Featured Title: " + featuredTitle.getText());
            System.out.println("The First Support Title: " + firstTopTitle.getText());
            System.out.println("The Second Support Title: " + secondSupportTitle.getText());
        }
    }

    private void typeSlowly(WebElement input, String text) throws InterruptedException {
        input.clear();
        for (char c : text.toCharArray()) {
            input.sendKeys(String.valueOf(c));
            Thread.sleep(75); // simulate real typing
        }
    }

    private void clickEnabledSubmitButton() {
        try {
            WebDriverWait buttonWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement submitButton = buttonWait.until(driver -> {
                WebElement btn = driver.findElement(By.cssSelector(
                    ".Button-module_btn__daEdK.SignInForm-module_btn__qTJMX.Button-module_alternate__iLJoF"
                ));
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
