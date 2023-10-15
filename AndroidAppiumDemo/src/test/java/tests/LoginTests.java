package tests;

import testBase.AppTestBase;
import testBase.PageDriver;
import pages.LoginPage;
import pages.ProductsPage;
import utils.TestUtils;

import org.testng.Assert;
import org.testng.annotations.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class LoginTests extends AppTestBase{
	LoginPage loginPage;
	ProductsPage productsPage;
	JsonNode jsonNode;
	JsonNode invalidUser;
	JsonNode invalidPassword;
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
	  public void beforeMethod(Method m) {
		  utils.log().info("\n" + "****** starting test:" + m.getName() + "******" + "\n");
		  loginPage = new LoginPage();
		  invalidUser = jsonNode.get("invalidUser");
		  invalidPassword = jsonNode.get("invalidPassword");
		  validUser = jsonNode.get("validUser");
	  }

	  @AfterMethod
	  public void afterMethod() {	
		  
	  }
	  
	  @Test
	  public void invalidUserName() throws Exception {
		  loginPage.enterUserName(invalidUser.get("username").asText());
		  loginPage.enterPassword(invalidUser.get("password").asText());
		  loginPage.pressLoginBtn();
		  String actualErrTxt = loginPage.getErrTxt();
		  String expectedErrTxt = PageDriver.getStrings("err_invalid_username_or_password");
		  Assert.assertEquals(actualErrTxt, expectedErrTxt);
	  }
	  
	  @Test
	  public void invalidPassword() throws Exception {
		  loginPage.enterUserName(invalidPassword.get("username").asText());
		  loginPage.enterPassword(invalidPassword.get("password").asText());
		  loginPage.pressLoginBtn();	  
		  String actualErrTxt = loginPage.getErrTxt();
		  String expectedErrTxt = PageDriver.getStrings("err_invalid_username_or_password");
		  Assert.assertEquals(actualErrTxt, expectedErrTxt);
	  }
	  
	  @Test
	  public void successfulLogin() throws Exception {
		  loginPage.enterUserName(validUser.get("username").asText());
		  loginPage.enterPassword(validUser.get("password").asText());
		  productsPage = loginPage.pressLoginBtn();	  
		  String actualProductTitle = productsPage.getTitle();		  
		  String expectedProductTitle = PageDriver.getStrings("product_title");
		  Assert.assertEquals(actualProductTitle, expectedProductTitle);
	  }
}
