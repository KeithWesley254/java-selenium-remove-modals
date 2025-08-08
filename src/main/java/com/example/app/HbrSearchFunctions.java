package com.example.app;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.Assertions;

public class HbrSearchFunctions {
    private final WebDriver driver;
    private final WebDriverWait longWait;

    public HbrSearchFunctions(WebDriver driver) {
        this.driver = driver;
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void performSearchAndAssert(String query) {
        try {
            // Open the search UI
            WebElement searchToggle = longWait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label*='Search']"))
            );
            searchToggle.click();

            // Wait for the search input
            WebElement searchInput = longWait.until(
                ExpectedConditions.elementToBeClickable(By.name("term"))
            );

            // Clear and type query
            searchInput.clear();
            GenUtils.typeSlowly(searchInput, query);
            searchInput.sendKeys(Keys.ENTER);

            // Wait for title to contain query
            longWait.until(ExpectedConditions.titleContains(query));

            String newPageTitle = driver.getTitle();

            // Normalize the dashes for comparison
            String normalizedTitle = GenUtils.normalizeDashes(newPageTitle);
            String normalizedQuery = GenUtils.normalizeDashes(query);

            Assertions.assertTrue(
                normalizedTitle.toLowerCase().contains(normalizedQuery.toLowerCase()),
                "New page title does not contain the search query. Expected query: " 
                + query + " | Actual title: " + newPageTitle
            );
            System.out.println("Verified new page title after search: " + newPageTitle);

            // Assert the search results count
            WebElement resultsHeader = longWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("h3[aria-label*='search results found']"))
            );

            String resultsText = resultsHeader.getAttribute("aria-label");
            int actualResultCount = GenUtils.extractNumberFromText(resultsText);

            Assertions.assertTrue(
                actualResultCount > 0,
                "Expected at least 1 search result, but got: " + actualResultCount
            );

            System.out.println("Successfully found search results count: " + actualResultCount);

            // Get and print the first search result title
            WebElement firstResultTitle = longWait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("a[js-target='search-item']")
                )
            );
            String firstTitleText = firstResultTitle.getText();
            System.out.println("First search result title: " + firstTitleText);

        } catch (Exception e) {
            throw new RuntimeException("Failed to perform search for query: " + query, e);
        }
    }

}
