import os
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils import save_screenshot

BASE_URL = "https://www.pixelssuite.com/"
TEST_IMAGE = r"E:\SeleniumTestImages\test.png"

def test_resize_feature_valid_image(driver):
    assert os.path.exists(TEST_IMAGE), f"Test image not found: {TEST_IMAGE}"

    driver.get(BASE_URL)

    WebDriverWait(driver, 15).until(
        EC.element_to_be_clickable((By.XPATH, "//button[contains(., 'Resize')]"))
    ).click()

    WebDriverWait(driver, 15).until(
        EC.presence_of_element_located((By.TAG_NAME, "input"))
    )

    file_inputs = driver.find_elements(By.XPATH, "//input[@type='file']")
    assert file_inputs, "No file input found on resize page"

    file_inputs[0].send_keys(TEST_IMAGE)

    number_inputs = driver.find_elements(By.XPATH, "//input[@type='number']")
    if len(number_inputs) >= 2:
        number_inputs[0].clear()
        number_inputs[0].send_keys("300")
        number_inputs[1].clear()
        number_inputs[1].send_keys("300")

    buttons = driver.find_elements(By.TAG_NAME, "button")
    for btn in buttons:
        if btn.text.strip().lower() == "resize":
            btn.click()
            break

    save_screenshot(driver, "TC07_resize_valid")

    page_text = driver.page_source.lower()
    assert "download" in page_text or "resized" in page_text or "success" in page_text