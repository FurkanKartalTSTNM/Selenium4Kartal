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
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleSearchTest {
    private RemoteWebDriver driver;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        driver = new TestiniumSeleniumDriver(new URL("http://172.25.1.110:4444/wd/hub"),options);
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

    @Test
    public void runDefaultTest2() throws MalformedURLException, InterruptedException {
        ChromeOptions options =  new ChromeOptions();
        // Testinium anahtarı gerekiyorsa Options üstünden ver:

        // İstersen browser'ı env'den oku
        //String browserName = System.getenv("browser");

        // RemoteWebDriver yerine kendi TestiniumSeleniumDriver'ını options ile başlat
        RemoteWebDriver driver = new TestiniumSeleniumDriver(new URL("http://172.25.1.110:4444/wd/hub"), options);

        driver.get("https://www.amazon.com");
        System.out.println("Page title: " + driver.getTitle());
        Thread.sleep(5000); // 3 saniye bekler


        driver.quit();
    }

}
