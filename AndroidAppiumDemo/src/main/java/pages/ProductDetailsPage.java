package pages;

import testBase.MenuPage;
import testBase.PageDriver;
import utils.TestUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class ProductDetailsPage extends MenuPage {
	TestUtils utils = new TestUtils();
	
	@AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]\n" + 
			"") 
	@iOSXCUITFindBy (xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/child::XCUIElementTypeStaticText[1]")
	private WebElement SLBTitle;
	
	@AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]"
			+ "") 
	@iOSXCUITFindBy (xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/child::XCUIElementTypeStaticText[2]")
	private WebElement SLBTxt;
		
	@AndroidFindBy (accessibility = "test-BACK TO PRODUCTS") 
	@iOSXCUITFindBy (id = "test-BACK TO PRODUCTS")
	private WebElement backToProductsBtn;
	
	@iOSXCUITFindBy (id = "test-ADD TO CART") private WebElement addToCartBtn;

	public String getSLBTitle() {
		String title = getText(SLBTitle, "title is - ");
		return title;
	}

	public String getSLBTxt() {
		String txt = getText(SLBTxt, "txt is - ");
		return txt;
	}

	public WebElement scrollToSLBPriceAndGetSLBPrice() {
		WebElement el = (WebElement) PageDriver.getDriver().findElement(By.linkText("new UiScrollable("
				+ "new UiSelector().scrollable(true)).scrollIntoView(" + "new UiSelector().text(\"" + "$29.99" + "\"));"));
		return el;
	}

	public void scrollPage() {
		iOSScrollToElement();
	}

	public Boolean isAddToCartBtnDisplayed() {
		return addToCartBtn.isDisplayed();
	}

	public ProductsPage pressBackToProductsBtn() {
		click(backToProductsBtn, "navigate back to products page");
		return new ProductsPage();
	}

}
