from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils import save_screenshot

BASE_URL = "https://www.pixelssuite.com/"

def test_footer_links_visible(driver):
    driver.get(BASE_URL)

    driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")

    WebDriverWait(driver, 10).until(
        EC.presence_of_all_elements_located((By.TAG_NAME, "a"))
    )

    links = driver.find_elements(By.TAG_NAME, "a")
    texts = [link.text.strip().lower() for link in links if link.text.strip()]

    expected_footer_links = [
        "about us",
        "contact",
        "privacy policy",
        "terms of service",
        "disclaimer"
    ]

    missing = [item for item in expected_footer_links if item not in texts]

    print("Footer links found:", texts)
    save_screenshot(driver, "TC03_footer_links")

    assert not missing, f"Missing footer links: {missing}"