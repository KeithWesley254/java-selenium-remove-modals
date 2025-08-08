package com.example.app;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class BrowserStorage {

    public static WebDriver createDriver(String browser, String remoteUrl) throws Exception {
        switch (browser.toLowerCase()) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("-private", "--disable-extensions");
                firefoxOptions.addArguments("--start-maximized");
                firefoxOptions.addPreference("browser.privatebrowsing.autostart", true);
                return new RemoteWebDriver(new URL(remoteUrl), firefoxOptions);

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--inprivate", "--disable-extensions");
                edgeOptions.addArguments("--start-maximized");
                return new RemoteWebDriver(new URL(remoteUrl), edgeOptions);

            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--incognito", "--disable-extensions");
                chromeOptions.addArguments("--start-maximized");
                return new RemoteWebDriver(new URL(remoteUrl), chromeOptions);

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
}