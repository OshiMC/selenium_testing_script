from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils import save_screenshot

BASE_URL = "https://www.pixelssuite.com/"

def test_homepage_key_menu_buttons_visible(driver):
    driver.get(BASE_URL)

    WebDriverWait(driver, 20).until(
        EC.presence_of_element_located((By.TAG_NAME, "body"))
    )

    body = driver.find_element(By.TAG_NAME, "body")
    page_text = body.text.lower()

    expected = [
        "document converter",
        "editor",
        "resize",
        "crop",
        "compress"
    ]

    missing = [item for item in expected if item not in page_text]

    print("Page text preview:", page_text[:1000])
    print("Missing:", missing)

    save_screenshot(driver, "TC02_homepage_menu")
    assert not missing, f"Missing homepage items: {missing}"