import pytest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains

@pytest.fixture
def driver():
    # Initialize WebDriver
    driver = webdriver.Chrome()
    yield driver
    # Close the browser
    driver.quit()

# Generic function to login
def login(driver, username, password):
    driver.get("https://www.saucedemo.com/")
    driver.find_element(By.ID, "user-name").send_keys(username)
    driver.find_element(By.ID, "password").send_keys(password)
    driver.find_element(By.ID, "login-button").click()

# Generic function to add an item to the cart
def add_item_to_cart(driver):
    driver.find_element(By.CSS_SELECTOR, ".inventory_item:nth-child(1) .btn_primary").click()

# Generic function to verify an item is added to the cart
def verify_item_added_to_cart(driver):
    cart_badge = driver.find_element(By.CLASS_NAME, "shopping_cart_badge")
    assert cart_badge.text == "1", "Item was not added to the cart."
    if cart_badge.text == "1":
        print('item is added to the cart validated')
    else:
        print('item is not added to the cart')

# Generic function to log out
def logout(driver):
    menu_button = driver.find_element(By.ID, "react-burger-menu-btn")
    logout_button = driver.find_element(By.ID, "logout_sidebar_link")
    menu_button.click()
    actions = ActionChains(driver)
    actions.move_to_element(menu_button).move_to_element(logout_button).click().perform()

def test_saucedemo_scenario(driver):
    driver.implicitly_wait(10)
    driver.maximize_window()

    # Login
    login(driver, "standard_user", "secret_sauce")

    # Verify Swag Labs is present on the webpage
    assert "Swag Labs" in driver.title
    if driver.title == "Swag Labs":
        print('Title verified')
    else:
        print('Wrong Title')

    # Add an item to the cart
    add_item_to_cart(driver)

    # Verify item is added to the cart
    verify_item_added_to_cart(driver)

    # Logout
    logout(driver)