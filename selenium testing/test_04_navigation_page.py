from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils import save_screenshot

BASE_URL = "https://www.pixelssuite.com/"

def test_about_us_navigation(driver):
    driver.get(BASE_URL)

    about_link = WebDriverWait(driver, 15).until(
        EC.element_to_be_clickable((By.LINK_TEXT, "About Us"))
    )
    about_link.click()

    WebDriverWait(driver, 15).until(
        EC.presence_of_element_located((By.TAG_NAME, "body"))
    )

    save_screenshot(driver, "TC04_about_us")
    assert "about" in driver.title.lower() or "about" in driver.page_source.lower()


def test_contact_navigation(driver):
    driver.get(BASE_URL)

    contact_link = WebDriverWait(driver, 15).until(
        EC.element_to_be_clickable((By.LINK_TEXT, "Contact"))
    )
    contact_link.click()

    WebDriverWait(driver, 15).until(
        EC.presence_of_element_located((By.TAG_NAME, "body"))
    )

    save_screenshot(driver, "TC04_contact")
    assert "contact" in driver.title.lower() or "contact" in driver.page_source.lower()