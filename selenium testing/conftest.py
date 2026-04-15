import pytest
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager

BASE_URL = "https://www.pixelssuite.com/"

@pytest.fixture(scope="function")
def driver():
    options = webdriver.ChromeOptions()
    options.add_argument("--start-maximized")
    options.add_experimental_option("excludeSwitches", ["enable-logging"])

    drv = webdriver.Chrome(
        service=Service(ChromeDriverManager().install()),
        options=options
    )
    drv.implicitly_wait(5)
    yield drv
    drv.quit()