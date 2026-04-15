from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils import save_screenshot

def test_result_contains_download_button(driver):
    driver.get("https://www.pixelssuite.com/")

    WebDriverWait(driver, 15).until(
        EC.presence_of_element_located((By.TAG_NAME, "body"))
    )

    page_text = driver.page_source.lower()

    save_screenshot(driver, "TC08_result_download_check")

    assert "download" in page_text