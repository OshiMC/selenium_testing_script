package com.pixelstest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class TC_01 {

    WebDriver driver;
    WebDriverWait wait;

    final String BASE_URL = "https://www.pixelssuite.com/";

    final String TEST_IMAGE_JPG = "E:\\SeleniumTestImages\\test.jpg";
    final String TEST_IMAGE_PNG = "E:\\SeleniumTestImages\\test.png";
    final String TEST_IMAGE_WEBP = "E:\\SeleniumTestImages\\test.webp";
    final String TEST_IMAGE_PDF = "E:\\SeleniumTestImages\\test.pdf";

    final String SCREENSHOT_FOLDER = "E:\\SeleniumTestImages\\screenshots\\";

    // Setup
    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Take screenshot
    private void takeScreenshot(String testName) {
        try {
            File folder = new File(SCREENSHOT_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(SCREENSHOT_FOLDER + testName + ".png");
            FileUtils.copyFile(src, dest);

            System.out.println("Screenshot saved: " + dest.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }

    // Upload file
    private void uploadFile(String filePath) {
        WebElement fileInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']"))
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].classList.remove('hidden'); arguments[0].style.display='block';",
                fileInput
        );

        fileInput.sendKeys(filePath);
    }

    // Wait for text
    private boolean waitForText(String keyword, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"
                                    + keyword.toLowerCase() + "')]")
                    )
            );
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Homepage test
    @Test(priority = 1)
    public void TC_A01_HomepageLoads() {
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        String title = driver.getTitle();
        System.out.println("[TC_A01] Page title: " + title);

        Assert.assertFalse(title.isEmpty(), "Homepage title should not be empty");
        takeScreenshot("TC_A01_HomepageLoads");
    }

    @Test(priority = 2)
    public void TC_A02_AllFeaturesVisibleOnHomepage() {
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        String body = driver.findElement(By.tagName("body")).getText().toLowerCase();

        Assert.assertTrue(body.contains("resize"), "Homepage should show Resize feature");
        Assert.assertTrue(body.contains("compress"), "Homepage should show Compress feature");
        Assert.assertTrue(body.contains("convert"), "Homepage should show Convert feature");
        Assert.assertTrue(body.contains("crop"), "Homepage should show Crop feature");

        takeScreenshot("TC_A02_AllFeaturesVisible");
    }

    // Teardown
    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}