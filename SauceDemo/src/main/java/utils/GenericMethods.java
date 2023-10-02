
package utils;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import
org.openqa.selenium.support.ui.ExpectedConditions;
import
org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import base.PageDriver;
import pageObjects.LoginPage;

public class GenericMethods {
	WebDriver driver = PageDriver.getDriver();
	LoginPage loginPageObj = new LoginPage();
	public void performLogin(String username, String password) {
		this.driver = PageDriver.getDriver();
		loginPageObj.setUsername().sendKeys(username);
		loginPageObj.setPassword().sendKeys(password);
		loginPageObj.setLoginButton().click();
		//using current URL to assert because Title is remaining same even after login
		Assert.assertTrue(driver.getCurrentUrl().equalsIgnoreCase("https://www.saucedemo.com/inventory.html"), "Login Failed");
	}


	public void assertPageTitle() {
		this.driver = PageDriver.getDriver();
		Assert.assertEquals(driver.getTitle().compareToIgnoreCase("Swag Labs"),0, "Page title verified");
	}


	public void goToHomePage() {
		this.driver = PageDriver.getDriver();
		try {
			if ((loginPageObj.setDropDownButton()).isDisplayed()) {
				loginPageObj.setDropDownButton().click();
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				wait.until(ExpectedConditions.elementToBeClickable(loginPageObj.setLogoutButton()));
				loginPageObj.setLogoutButton().click();
				wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/"));
			}
		} catch (Exception e) {
			System.out.println("Exception caught");
		}
	}
}
