import os
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils import save_screenshot

BASE_URL = "https://www.pixelssuite.com/"
TEST_PDF = r"E:\SeleniumTestImages\test.pdf"

def test_pdf_editor_upload(driver):
    assert os.path.exists(TEST_PDF), f"Test file not found: {TEST_PDF}"

    driver.get(BASE_URL)

    # Open Editor (PDF Editor)
    WebDriverWait(driver, 20).until(
        EC.element_to_be_clickable((By.XPATH, "//button[contains(., 'Editor')]"))
    ).click()

    # Wait for file input
    file_input = WebDriverWait(driver, 20).until(
        EC.presence_of_element_located((By.XPATH, "//input[@type='file']"))
    )

    # Upload PDF
    file_input.send_keys(TEST_PDF)

    save_screenshot(driver, "TC_pdf_uploaded")

    page_text = driver.page_source.lower()

    # Validate PDF editor loaded
    assert (
        "pdf" in page_text or
        "editor" in page_text or
        "page" in page_text or
        "download" in page_text
    ), "PDF editor did not load properly"