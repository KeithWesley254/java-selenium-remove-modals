package com.example.app;

import org.openqa.selenium.WebDriver;

public class MainTestRunner {

    public static void main(String[] args) {
        
        // Firefox container port 4444
        // String browser = "firefox";
        // String remoteUrl = "http://localhost:4444/wd/hub";

        // Edge Container port 4445
        String browser = "edge";
        String remoteUrl = "http://localhost:4445/wd/hub";

        // Google Chrome port 4446
        // String browser = "chrome";
        // String remoteUrl = "http://localhost:4446/wd/hub";

        WebDriver driver = null;

        try {
            driver = BrowserStorage.createDriver(browser, remoteUrl);

            driver.get("https://hbr.org/");

            // System.out.println("Page Title: " + driver.getTitle());
            // System.out.println("Current URL: " + driver.getCurrentUrl());

            HbrPageActions hbr = new HbrPageActions(driver);
            // HbrSearchFunctions hbrSearch = new HbrSearchFunctions(driver);

            hbr.closeInitialModalIfPresent();
            // hbr.signInWithFakeCredentialsAndRetry();
            // hbr.clickSubscribeAndVerifyRedirectInNewTab();
            // hbr.extract3Titles();
            hbr.checkHomepageTitle("Harvard Business Review - Ideas and Advice for Leaders");
            // hbrSearch.performSearchAndAssert("Trees");
          

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}