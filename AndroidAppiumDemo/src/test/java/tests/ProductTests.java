package tests;

import testBase.AppTestBase;
import testBase.PageDriver;
import pages.LoginPage;
import pages.ProductDetailsPage;
import pages.ProductsPage;
import pages.SettingsPage;
import utils.TestUtils;

import org.json.JSONException;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class ProductTests extends AppTestBase{
	LoginPage loginPage;
	ProductsPage productsPage;
	SettingsPage settingsPage;
	ProductDetailsPage productDetailsPage;
	JsonNode jsonNode;
	JsonNode validUser;
	TestUtils utils = new TestUtils();
	
	  @BeforeClass
	  public void beforeClass() throws Exception {
		  try {
			  String dataFileName = "C:/SDET_Intermediate/AndroidAppiumDemo/src/test/java/tests/loginUsers.json";
			  File file = new File(dataFileName);
			  InputStream inputStream = new FileInputStream(file);
			  InputStreamReader reader = new InputStreamReader(inputStream);
			  ObjectMapper objectMapper = new ObjectMapper();
			  jsonNode = objectMapper.readTree(reader);
		  } catch(Exception e) {
			  e.printStackTrace();
			  throw e;
		  }
		  closeApp("com.swaglabsmobileapp");
		  launchApp("com.swaglabsmobileapp","com.swaglabsmobileapp.SplashActivity");
	  }

	  @AfterClass
	  public void afterClass() {
	  }
	  
	  @BeforeMethod
	  public void beforeMethod(Method m) throws JSONException {
		  utils.log().info("\n" + "****** starting test:" + m.getName() + "******" + "\n");
		  loginPage = new LoginPage();	
		  validUser = jsonNode.get("validUser");
		  productsPage = loginPage.login(validUser.get("username").asText(), 
				  validUser.get("password").asText());
	  }

	  @AfterMethod
	  public void afterMethod() {
		  settingsPage = productsPage.pressSettingsBtn();
		  loginPage = settingsPage.pressLogoutBtn();
	  }
	  
	  @Test
	  public void validateProductOnProductsPage() throws Exception {
		  SoftAssert sa = new SoftAssert();
		  String SLBTitle = productsPage.getSLBTitle();
		  sa.assertEquals(SLBTitle, PageDriver.getStrings("products_page_slb_title"));
		  String SLBPrice = productsPage.getSLBPrice();
		  sa.assertEquals(SLBPrice, PageDriver.getStrings("products_page_slb_price"));
		  sa.assertAll();
	  }
}
