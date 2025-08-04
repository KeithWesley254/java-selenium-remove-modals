package com.example.app;

import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GetSiteDetails {

    public static void main(String[] args) throws Exception {

        // Firefox container port 4444
        // String browser = "firefox";
        // String remoteUrl = "http://localhost:4444/wd/hub";

        // Edge Container port 4445
        String browser = "edge";
        String remoteUrl = "http://localhost:4445/wd/hub";

        WebDriver driver;

        if (browser.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("-private", "--disable-extensions");
            options.addPreference("browser.privatebrowsing.autostart", true);
            driver = new RemoteWebDriver(new URL(remoteUrl), options);
        } else if (browser.equals("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--inprivate", "--disable-extensions");
            driver = new RemoteWebDriver(new URL(remoteUrl), options);
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        try {
            driver.get("https://hbr.org/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            System.out.println("Page Title: " + driver.getTitle());
            System.out.println("Current URL: " + driver.getCurrentUrl());

            // Handle modal if it appears
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
                        hideModal(driver, modal);
                    }
                } catch (NoSuchElementException ce) {
                    hideModal(driver, modal);
                }

                // Wait for modal to disappear
                Thread.sleep(2000);

            } catch (TimeoutException e) {
                System.out.println("No modal appeared.");
            }

            // Wait for the search section to be present
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form[action*='/search']")));

            // Click the search button
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[aria-label*='Search']")));
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[aria-label*='Search']")
            ));
            searchButton.click();

            // Wait for the input to be ready
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("term")));
            WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("term")));

            searchInput.sendKeys("Automation Testing");
            searchInput.sendKeys(Keys.ENTER);

            Thread.sleep(7000); // Let results load

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            driver.quit();
        }
    }

    private static void hideModal(WebDriver driver, WebElement modal) {
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
}