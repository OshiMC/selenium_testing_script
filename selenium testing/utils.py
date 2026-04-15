import os
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

SCREENSHOT_DIR = r"E:\SeleniumTestImages\screenshots"
os.makedirs(SCREENSHOT_DIR, exist_ok=True)

def save_screenshot(driver, name):
    path = os.path.join(SCREENSHOT_DIR, f"{name}.png")
    driver.save_screenshot(path)
    print(f"Screenshot saved: {path}")

def wait_for_title(driver, text, timeout=15):
    WebDriverWait(driver, timeout).until(EC.title_contains(text))