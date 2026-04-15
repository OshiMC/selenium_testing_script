package com.pixelstest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.File;
import java.time.Duration;

public class PixelsSuiteAutomationTest {

    WebDriver driver;
    WebDriverWait wait;
    final String BASE_URL = "https://www.pixelssuite.com/";

    final String TEST_IMAGE_JPG  = "E:\\SeleniumTestImages\\test.jpg";
    final String TEST_IMAGE_PNG  = "E:\\SeleniumTestImages\\test.png";
    final String TEST_IMAGE_WEBP = "E:\\SeleniumTestImages\\test.webp";
    final String TEST_IMAGE_PDF  = "E:\\SeleniumTestImages\\test.pdf";

    // ── Setup ────────────────────────────────────────────────────────────────
    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Helper: take screenshot ──────────────────────────────────────────────
    private void takeScreenshot(String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File("E:\\SeleniumTestImages\\screenshots\\" + testName + ".png"));
            System.out.println("Screenshot saved: " + testName);
        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }

    // ── Helper: upload file ──────────────────────────────────────────────────
    private void uploadFile(String filePath) {
        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='file']")
        ));
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].classList.remove('hidden'); arguments[0].style.display='block';",
            fileInput
        );
        fileInput.sendKeys(filePath);
    }

    // ── Helper: wait for text ────────────────────────────────────────────────
    private boolean waitForText(String keyword, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + keyword + "')]")
                )
            );
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SECTION 1 – HOMEPAGE TESTS
    // ══════════════════════════════════════════════════════════════════════════

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
        Assert.assertTrue(body.contains("resize"),   "Homepage should show Resize feature");
        Assert.assertTrue(body.contains("compress"), "Homepage should show Compress feature");
        Assert.assertTrue(body.contains("convert"),  "Homepage should show Convert feature");
        Assert.assertTrue(body.contains("crop"),     "Homepage should show Crop feature");
        takeScreenshot("TC_A02_AllFeaturesVisible");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SECTION 2 – RESIZE IMAGE TESTS
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 3)
    public void TC_A03_ResizePage_UploadValidJPG() {
        driver.get(BASE_URL + "resize-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_JPG);
        boolean success = waitForText("resize", 10)
                       || !driver.findElements(By.xpath("//img[contains(@class,'preview') or contains(@src,'blob')]")).isEmpty()
                       || !driver.findElements(By.xpath("//input[@type='number']")).isEmpty();
        System.out.println("[TC_A03] JPG upload success: " + success);
        Assert.assertTrue(success, "After uploading JPG, resize options or preview should appear");
        takeScreenshot("TC_A03_ResizeUploadJPG");
    }

    @Test(priority = 4)
    public void TC_A04_ResizePage_UploadValidPNG() {
        driver.get(BASE_URL + "resize-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_PNG);
        boolean success = waitForText("resize", 10)
                       || !driver.findElements(By.xpath("//input[@type='number']")).isEmpty();
        System.out.println("[TC_A04] PNG upload success: " + success);
        Assert.assertTrue(success, "After uploading PNG, resize options should appear");
        takeScreenshot("TC_A04_ResizeUploadPNG");
    }

    @Test(priority = 5)
    public void TC_A05_ResizePage_UploadInvalidFile_PDF() {
        driver.get(BASE_URL + "resize-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_PDF);
        boolean errorShown = waitForText("invalid", 5)
                          || waitForText("error", 5)
                          || waitForText("supported", 5)
                          || waitForText("unsupported", 5);
        System.out.println("[TC_A05] PDF rejection error shown: " + errorShown);
        if (!errorShown) {
            System.out.println("[TC_A05] DEFECT: No error message shown for unsupported file type");
        }
        takeScreenshot("TC_A05_ResizeUploadPDF");
        Assert.assertTrue(errorShown, "Uploading a PDF should show an error message");
    }

    @Test(priority = 6)
    public void TC_A06_ResizePage_WidthInputAcceptsNumber() {
        driver.get(BASE_URL + "resize-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_JPG);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='number']")));
        WebElement widthInput = driver.findElements(By.xpath("//input[@type='number']")).get(0);
        widthInput.clear();
        widthInput.sendKeys("800");
        String value = widthInput.getAttribute("value");
        System.out.println("[TC_A06] Width input value: " + value);
        Assert.assertEquals(value, "800", "Width input should accept value 800");
        takeScreenshot("TC_A06_ResizeWidthInput");
    }

    @Test(priority = 7)
    public void TC_A07_ResizePage_HeightInputAcceptsNumber() {
        driver.get(BASE_URL + "resize-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_JPG);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='number']")));
        WebElement heightInput = driver.findElements(By.xpath("//input[@type='number']")).get(1);
        heightInput.clear();
        heightInput.sendKeys("600");
        String value = heightInput.getAttribute("value");
        System.out.println("[TC_A07] Height input value: " + value);
        Assert.assertEquals(value, "600", "Height input should accept value 600");
        takeScreenshot("TC_A07_ResizeHeightInput");
    }

    @Test(priority = 8)
    public void TC_A08_ResizePage_DownloadButtonAppearsAfterResize() {
        driver.get(BASE_URL + "resize-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_JPG);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='number']")));
        WebElement widthInput = driver.findElements(By.xpath("//input[@type='number']")).get(0);
        widthInput.clear();
        widthInput.sendKeys("300");
        WebElement resizeBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'resize')]")
        ));
        resizeBtn.click();
        boolean downloadVisible = waitForText("download", 10)
            || !driver.findElements(By.xpath("//a[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'download')]")).isEmpty()
            || !driver.findElements(By.xpath("//button[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'download')]")).isEmpty();
        System.out.println("[TC_A08] Download button visible: " + downloadVisible);
        Assert.assertTrue(downloadVisible, "Download button should appear after resize");
        takeScreenshot("TC_A08_ResizeDownloadButton");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SECTION 3 – COMPRESS IMAGE TESTS
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 9)
    public void TC_A09_CompressPage_UploadValidJPG() {
        driver.get(BASE_URL + "compress-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_JPG);
        boolean success = waitForText("compress", 10)
                       || waitForText("download", 10)
                       || waitForText("quality", 10)
                       || !driver.findElements(By.xpath("//input[@type='range']")).isEmpty();
        System.out.println("[TC_A09] Compress JPG upload success: " + success);
        Assert.assertTrue(success, "After uploading JPG to compress page, options should appear");
        takeScreenshot("TC_A09_CompressUploadJPG");
    }

    @Test(priority = 10)
    public void TC_A10_CompressPage_UploadValidPNG() {
        driver.get(BASE_URL + "compress-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_PNG);
        boolean success = waitForText("compress", 10)
                       || waitForText("download", 10)
                       || !driver.findElements(By.xpath("//input[@type='range']")).isEmpty();
        System.out.println("[TC_A10] Compress PNG upload success: " + success);
        Assert.assertTrue(success, "After uploading PNG to compress page, options should appear");
        takeScreenshot("TC_A10_CompressUploadPNG");
    }

    @Test(priority = 11)
    public void TC_A11_CompressPage_UploadInvalidFile_PDF() {
        driver.get(BASE_URL + "compress-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_PDF);
        boolean errorShown = waitForText("invalid", 5)
                          || waitForText("error", 5)
                          || waitForText("supported", 5);
        System.out.println("[TC_A11] Compress PDF rejection shown: " + errorShown);
        if (!errorShown) {
            System.out.println("[TC_A11] DEFECT: No error shown for unsupported PDF upload");
        }
        takeScreenshot("TC_A11_CompressUploadPDF");
        Assert.assertTrue(errorShown, "Uploading PDF to compress page should show an error");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SECTION 4 – CONVERT IMAGE TESTS
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 12)
    public void TC_A12_ConvertPage_UploadValidJPG() {
        driver.get(BASE_URL + "convert-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_JPG);
        boolean success = waitForText("convert", 10)
                       || waitForText("format", 10)
                       || waitForText("png", 10)
                       || !driver.findElements(By.xpath("//select")).isEmpty();
        System.out.println("[TC_A12] Convert JPG upload success: " + success);
        Assert.assertTrue(success, "After uploading JPG to convert page, format options should appear");
        takeScreenshot("TC_A12_ConvertUploadJPG");
    }

    @Test(priority = 13)
    public void TC_A13_ConvertPage_UploadValidPNG() {
        driver.get(BASE_URL + "convert-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_PNG);
        boolean success = waitForText("convert", 10)
                       || waitForText("format", 10)
                       || !driver.findElements(By.xpath("//select")).isEmpty();
        System.out.println("[TC_A13] Convert PNG upload success: " + success);
        Assert.assertTrue(success, "After uploading PNG to convert page, format options should appear");
        takeScreenshot("TC_A13_ConvertUploadPNG");
    }

    @Test(priority = 14)
    public void TC_A14_ConvertPage_UploadInvalidFile_PDF() {
        driver.get(BASE_URL + "convert-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_PDF);
        boolean errorShown = waitForText("invalid", 5)
                          || waitForText("error", 5)
                          || waitForText("supported", 5);
        System.out.println("[TC_A14] Convert PDF rejection shown: " + errorShown);
        if (!errorShown) {
            System.out.println("[TC_A14] DEFECT: No error shown for unsupported PDF on Convert page");
        }
        takeScreenshot("TC_A14_ConvertUploadPDF");
        Assert.assertTrue(errorShown, "Uploading PDF to convert page should show an error");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SECTION 5 – CROP IMAGE TESTS
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 15)
    public void TC_A15_CropPage_UploadValidJPG() {
        driver.get(BASE_URL + "crop-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_JPG);
        boolean success = waitForText("crop", 10)
                       || waitForText("download", 10)
                       || !driver.findElements(By.xpath("//canvas")).isEmpty()
                       || !driver.findElements(By.xpath("//*[contains(@class,'crop')]")).isEmpty();
        System.out.println("[TC_A15] Crop JPG upload success: " + success);
        Assert.assertTrue(success, "After uploading JPG to crop page, crop tool should appear");
        takeScreenshot("TC_A15_CropUploadJPG");
    }

    @Test(priority = 16)
    public void TC_A16_CropPage_UploadValidPNG() {
        driver.get(BASE_URL + "crop-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_PNG);
        boolean success = waitForText("crop", 10)
                       || waitForText("download", 10)
                       || !driver.findElements(By.xpath("//canvas")).isEmpty();
        System.out.println("[TC_A16] Crop PNG upload success: " + success);
        Assert.assertTrue(success, "After uploading PNG to crop page, crop tool should appear");
        takeScreenshot("TC_A16_CropUploadPNG");
    }

    @Test(priority = 17)
    public void TC_A17_CropPage_UploadInvalidFile_PDF() {
        driver.get(BASE_URL + "crop-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        uploadFile(TEST_IMAGE_PDF);
        boolean errorShown = waitForText("invalid", 5)
                          || waitForText("error", 5)
                          || waitForText("supported", 5);
        System.out.println("[TC_A17] Crop PDF rejection shown: " + errorShown);
        if (!errorShown) {
            System.out.println("[TC_A17] DEFECT: No error shown for unsupported PDF on Crop page");
        }
        takeScreenshot("TC_A17_CropUploadPDF");
        Assert.assertTrue(errorShown, "Uploading PDF to crop page should show an error");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SECTION 6 – NAVIGATION & UI TESTS
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 18)
    public void TC_A18_NoPageHas404Error() {
        String[] pages = {
            "", "resize-image/", "compress-image/", "convert-image/", "crop-image/"
        };
        for (String page : pages) {
            driver.get(BASE_URL + page);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            String body = driver.findElement(By.tagName("body")).getText().toLowerCase();
            boolean has404 = body.contains("404") || body.contains("page not found");
            System.out.println("[TC_A18] Page '" + page + "' 404 found: " + has404);
            Assert.assertFalse(has404, "Page '" + page + "' should not show 404");
        }
        takeScreenshot("TC_A18_No404Error");
    }

    @Test(priority = 19)
    public void TC_A19_BackNavigationWorks() {
        driver.get(BASE_URL + "resize-image/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        driver.navigate().back();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        System.out.println("[TC_A19] After back navigation: " + driver.getCurrentUrl());
        Assert.assertFalse(driver.getCurrentUrl().isEmpty(), "Back navigation should work");
        takeScreenshot("TC_A19_BackNavigation");
    }

    @Test(priority = 20)
    public void TC_A20_AllPagesLoadUnder15Seconds() {
        String[] pages = {
            "resize-image/", "compress-image/", "convert-image/", "crop-image/"
        };
        for (String page : pages) {
            long start = System.currentTimeMillis();
            driver.get(BASE_URL + page);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            long loadTime = System.currentTimeMillis() - start;
            System.out.println("[TC_A20] Page '" + page + "' loaded in " + loadTime + "ms");
            Assert.assertTrue(loadTime < 15000, "Page '" + page + "' took too long: " + loadTime + "ms");
        }
        takeScreenshot("TC_A20_PageLoadTimes");
    }

    // ── Teardown ─────────────────────────────────────────────────────────────
    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}