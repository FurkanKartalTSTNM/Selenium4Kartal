package com.testinium.selenium4javakartal;

import com.testinium.driver.TestiniumSeleniumDriver;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleSearchTest {
    private RemoteWebDriver driver;


    @Test
    public void searchSelenium() {
        // Dismiss cookie consent if present
        try {
            WebElement consentButton = driver.findElement(By.xpath("//div[contains(@class,'VfPpkd-RLmnJb') or @id='L2AGLb']"));
            if (consentButton.isDisplayed()) {
                consentButton.click();
            }
        } catch (Exception ignored) {}

        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Selenium");
        searchBox.submit();

        WebElement results = driver.findElement(By.id("search"));
        assertTrue(results.isDisplayed());
        assertTrue(driver.getTitle().toLowerCase().contains("selenium"));
    }

    @Test
    public void runDefaultTest2() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");

        RemoteWebDriver driver = new TestiniumSeleniumDriver(
                new URL("http://host.docker.internal:4444/wd/hub"),
                options
        );

        try {
            driver.get("data:text/html,<title>SeleniumSmoke</title><h1 id='status'>OK</h1>");

            assertEquals("SeleniumSmoke", driver.getTitle());
            assertEquals("OK", driver.findElement(By.id("status")).getText());
        } finally {
            driver.quit();
        }
    }

}
