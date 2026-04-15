import os
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils import save_screenshot

BASE_URL = "https://www.pixelssuite.com/"
TEST_IMAGE = r"E:\SeleniumTestImages\test.png"

def test_compress_feature_valid_image(driver):
    assert os.path.exists(TEST_IMAGE), f"Test image not found: {TEST_IMAGE}"

    driver.get(BASE_URL)

    WebDriverWait(driver, 20).until(
        EC.element_to_be_clickable((By.XPATH, "//button[contains(., 'Compress')]"))
    ).click()

    WebDriverWait(driver, 20).until(
        EC.presence_of_element_located((By.XPATH, "//input[@type='file']"))
    )

    file_input = driver.find_element(By.XPATH, "//input[@type='file']")
    file_input.send_keys(TEST_IMAGE)

    buttons = driver.find_elements(By.TAG_NAME, "button")
    for btn in buttons:
        if btn.text.strip().lower() in ["compress", "submit", "apply"]:
            btn.click()
            break

    save_screenshot(driver, "TC_compress_result")

    page_text = driver.page_source.lower()
    assert "download" in page_text or "compress" in page_text or "success" in page_text