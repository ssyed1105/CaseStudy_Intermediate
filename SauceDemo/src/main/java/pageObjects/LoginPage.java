package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.PageDriver;
public class LoginPage {
	WebDriver driver;
	public void Loginpage(WebDriver driver) {
		this.driver = PageDriver.getDriver();
	}

	public WebElement setUsername() {
		WebElement username = PageDriver.getDriver().findElement(By.id("user-name"));
		return username;
	}

	public WebElement setPassword() {
		WebElement password = PageDriver.getDriver().findElement(By.id("password"));
		return password;
	}

	public WebElement setLoginButton() {
		WebElement loginButton = PageDriver.getDriver().findElement(By.id("login-button"));
		return loginButton;
	}

	public WebElement setErrorMessage() {
		WebElement errorMessage = PageDriver.getDriver().findElement(By.xpath("//*[@id=\\\"login_button_container\\\"]/div/form/h3/text()[1]"));
		return errorMessage;
	}

	public WebElement setDropDownButton() {
		WebElement dropDownButton = null;
		try {
			dropDownButton = PageDriver.getDriver().findElement(By.id("react-burger-menu-btn"));
		} catch(Exception e) {
			System.out.println("Exception caught");
		}
		return dropDownButton;
	}

	public WebElement setLogoutButton() {
		WebElement logoutButton = PageDriver.getDriver().findElement(By.xpath("//*[@id=\"logout_sidebar_link\"]"));
		return logoutButton;
	}
}
