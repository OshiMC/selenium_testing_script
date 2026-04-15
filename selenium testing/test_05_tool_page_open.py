from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils import save_screenshot

BASE_URL = "https://www.pixelssuite.com/"

def open_home(driver):
    driver.get(BASE_URL)
    WebDriverWait(driver, 15).until(
        EC.presence_of_element_located((By.TAG_NAME, "body"))
    )

def test_document_converter_button_click(driver):
    open_home(driver)

    btn = WebDriverWait(driver, 15).until(
        EC.element_to_be_clickable((By.XPATH, "//button[contains(., 'Document Converter')]"))
    )
    btn.click()

    save_screenshot(driver, "TC05_document_converter_open")
    assert "document" in driver.page_source.lower() or "converter" in driver.page_source.lower()


def test_editor_button_click(driver):
    open_home(driver)

    btn = WebDriverWait(driver, 15).until(
        EC.element_to_be_clickable((By.XPATH, "//button[contains(., 'Editor')]"))
    )
    btn.click()

    save_screenshot(driver, "TC05_editor_open")
    assert "editor" in driver.page_source.lower()


def test_resize_button_click(driver):
    open_home(driver)

    btn = WebDriverWait(driver, 15).until(
        EC.element_to_be_clickable((By.XPATH, "//button[contains(., 'Resize')]"))
    )
    btn.click()

    save_screenshot(driver, "TC05_resize_open")
    assert "resize" in driver.page_source.lower()