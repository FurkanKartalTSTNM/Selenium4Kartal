package com.testinium.selenium4javakartal;

import com.testinium.driver.TestiniumSeleniumDriver;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleSearchTest {
    private static final String EXECUTOR_UPLOAD_PATH = "/app/uploads/selenium4-upload-test.xlsx";
    private static final String EMPTY_EXCEL_UPLOAD_PATH = "/app/uploads/DenemeExcel.xlsx";

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

    @Test
    public void verifyUploadedExcelFile() throws IOException {
        Path uploadedExcel = Path.of(System.getProperty("uploadedExcelPath", EXECUTOR_UPLOAD_PATH));

        assertTrue(
                Files.isRegularFile(uploadedExcel),
                () -> "Uploaded Excel file was not found: " + uploadedExcel
        );
        assertTrue(Files.size(uploadedExcel) > 0, "Uploaded Excel file is empty");

        try (InputStream inputStream = Files.newInputStream(uploadedExcel);
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheet("UploadData");

            assertNotNull(sheet, "UploadData sheet was not found");
            assertEquals("SELENIUM4_UPLOAD_001", sheet.getRow(5).getCell(1).getStringCellValue());
            assertEquals("selenium4-linux", sheet.getRow(6).getCell(1).getStringCellValue());
            assertEquals("ready", sheet.getRow(7).getCell(1).getStringCellValue());
            assertEquals(EXECUTOR_UPLOAD_PATH, sheet.getRow(8).getCell(1).getStringCellValue());
            assertEquals(4, (int) sheet.getRow(9).getCell(1).getNumericCellValue());
        }
    }

    @Test
    public void verifyUploadedEmptyExcelFile() throws IOException {
        Path uploadedExcel = Path.of(System.getProperty("emptyUploadedExcelPath", EMPTY_EXCEL_UPLOAD_PATH));

        assertTrue(
                Files.isRegularFile(uploadedExcel),
                () -> "Uploaded empty Excel file was not found: " + uploadedExcel
        );
        assertTrue(Files.size(uploadedExcel) > 0, "Uploaded Excel file is empty on disk");

        try (InputStream inputStream = Files.newInputStream(uploadedExcel);
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            assertEquals(1, workbook.getNumberOfSheets(), "Unexpected worksheet count");

            Sheet sheet = workbook.getSheetAt(0);
            assertEquals("Sheet1", sheet.getSheetName());
            assertFalse(hasNonBlankCell(sheet), "The uploaded Excel worksheet contains data");
        }
    }

    private boolean hasNonBlankCell(Sheet sheet) {
        DataFormatter formatter = new DataFormatter();

        for (Row row : sheet) {
            for (Cell cell : row) {
                if (!formatter.formatCellValue(cell).isBlank()) {
                    return true;
                }
            }
        }

        return false;
    }

}
