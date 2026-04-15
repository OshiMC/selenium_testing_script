from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils import save_screenshot

BASE_URL = "https://www.pixelssuite.com/"

def test_homepage_loads(driver):
    driver.get(BASE_URL)

    WebDriverWait(driver, 15).until(
        EC.presence_of_element_located((By.TAG_NAME, "body"))
    )

    assert "Pixels" in driver.title
    save_screenshot(driver, "TC01_homepage_load")