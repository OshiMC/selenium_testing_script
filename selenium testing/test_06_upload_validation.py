from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils import save_screenshot

BASE_URL = "https://www.pixelssuite.com/"

def test_resize_without_upload(driver):
    driver.get(BASE_URL)

    WebDriverWait(driver, 15).until(
        EC.element_to_be_clickable((By.XPATH, "//button[contains(., 'Resize')]"))
    ).click()

    WebDriverWait(driver, 15).until(
        EC.presence_of_element_located((By.TAG_NAME, "body"))
    )

    buttons = driver.find_elements(By.TAG_NAME, "button")

    for btn in buttons:
        if btn.text.strip().lower() in ["resize", "submit", "convert"]:
            btn.click()
            break

    save_screenshot(driver, "TC06_resize_without_upload")

    page_text = driver.page_source.lower()
    assert (
        "upload" in page_text
        or "select" in page_text
        or "file" in page_text
        or "image" in page_text
    )