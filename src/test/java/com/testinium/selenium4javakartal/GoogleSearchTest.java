package com.testinium.selenium4javakartal;

import com.testinium.driver.TestiniumSeleniumDriver;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleSearchTest {
    private RemoteWebDriver driver;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        driver = new TestiniumSeleniumDriver(new URL("http://172.25.1.110:4444/wd/hub"),options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.google.com/");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }


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

}
