import os
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils import save_screenshot

BASE_URL = "https://www.pixelssuite.com/"
TEST_IMAGE = r"E:\SeleniumTestImages\test.png"

def test_resize_large_values(driver):
    driver.get(BASE_URL)

    # open resize
    WebDriverWait(driver, 20).until(
        EC.element_to_be_clickable((By.XPATH, "//button[contains(., 'Resize')]"))
    ).click()

    # upload image
    file_input = WebDriverWait(driver, 20).until(
        EC.presence_of_element_located((By.XPATH, "//input[@type='file']"))
    )
    file_input.send_keys(TEST_IMAGE)

    # enter large values
    number_inputs = driver.find_elements(By.XPATH, "//input[@type='number']")
    if len(number_inputs) >= 2:
        number_inputs[0].clear()
        number_inputs[0].send_keys("9999999")
        number_inputs[1].clear()
        number_inputs[1].send_keys("9999999")

    # click resize
    buttons = driver.find_elements(By.TAG_NAME, "button")
    for btn in buttons:
        if btn.text.strip().lower() == "resize":
            btn.click()
            break

    save_screenshot(driver, "TC_resize_large_values")

    page_text = driver.page_source.lower()

    # CORRECT VALIDATION
    assert "preview" not in page_text, "Preview should NOT be generated for large values"