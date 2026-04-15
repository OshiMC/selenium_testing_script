import os
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils import save_screenshot

BASE_URL = "https://www.pixelssuite.com/"
TEST_IMAGE = r"E:\SeleniumTestImages\test.png"

def test_crop_feature_valid_image(driver):
    assert os.path.exists(TEST_IMAGE), f"Test image not found: {TEST_IMAGE}"

    driver.get(BASE_URL)

    WebDriverWait(driver, 20).until(
        EC.element_to_be_clickable((By.XPATH, "//button[contains(., 'Crop')]"))
    ).click()

    WebDriverWait(driver, 20).until(
        EC.presence_of_element_located((By.XPATH, "//input[@type='file']"))
    )

    file_input = driver.find_element(By.XPATH, "//input[@type='file']")
    file_input.send_keys(TEST_IMAGE)

    save_screenshot(driver, "TC_crop_uploaded")

    page_text = driver.page_source.lower()
    assert "crop" in page_text or "download" in page_text or "image" in page_text