package testBase;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import utils.TestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.Status;
import com.google.common.collect.ImmutableMap;
import reports.ExtentReport;

public class AppTestBase {
	TestUtils utils = new TestUtils();
	private static AppiumDriverLocalService server;
	
	public AppTestBase() {
		PageFactory.initElements(new AppiumFieldDecorator(PageDriver.getDriver()), this);
	}
	
	@BeforeMethod
	public void beforeMethod() {
		((CanRecordScreen) PageDriver.getDriver()).startRecordingScreen();
	}
	
	//stop video capturing and create *.mp4 file
	@AfterMethod
	public synchronized void afterMethod(ITestResult result) throws Exception {
		String media = ((CanRecordScreen) PageDriver.getDriver()).stopRecordingScreen();
		
		Map <String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();		
		String dirPath = "videos" + File.separator + params.get("platformName") + "_" + params.get("deviceName") 
		+ File.separator + PageDriver.getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();
		
		File videoDir = new File(dirPath);
		
		synchronized(videoDir){
			if(!videoDir.exists()) {
				videoDir.mkdirs();
			}	
		}
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
			stream.write(Base64.decodeBase64(media));
			stream.close();
			utils.log().info("video path: " + videoDir + File.separator + result.getName() + ".mp4");
		} catch (Exception e) {
			utils.log().error("error during video capture" + e.toString());
		} finally {
			if(stream != null) {
				stream.close();
			}
		}		
	}

	@BeforeSuite
	public void beforeSuite() throws Exception, Exception {
		/*
		 * ThreadContext.put("ROUTINGKEY", "ServerLogs"); server =
		 * getAppiumServerDefault(); if(!checkIfAppiumServerIsRunnning(4723) && server!=
		 * null) { server.start(); server.clearOutPutStreams();
		 * utils.log().info("Appium server started"); } else {
		 * utils.log().info("Appium server already running"); }
		 */
		utils.log().info("Appium server already running");
	}
	
	public boolean checkIfAppiumServerIsRunnning(int port) throws Exception {
	    boolean isAppiumServerRunning = false;
	    ServerSocket socket;
	    try {
	        socket = new ServerSocket(port);
	        socket.close();
	    } catch (IOException e) {
	    	System.out.println("1");
	        isAppiumServerRunning = true;
	    } finally {
	        socket = null;
	    }
	    return isAppiumServerRunning;
	}
	
	@AfterSuite (alwaysRun = true)
	public void afterSuite() {
		if(server != null && server.isRunning()) {
			server.stop();
			utils.log().info("Appium server stopped");
		}
	}

	public AppiumDriverLocalService getAppiumServerDefault() {
		return AppiumDriverLocalService.buildDefaultService();
	}
	
	@Parameters({"emulator", "platformName", "udid", "deviceName", "systemPort", "chromeDriverPort", "wdaLocalPort", "webkitDebugProxyPort"})
	@BeforeTest
	public void beforeTest(@Optional("androidOnly")String emulator, String platformName, String udid, String deviceName, @Optional("androidOnly")String systemPort, @Optional("androidOnly")String chromeDriverPort, @Optional("iOSOnly")String wdaLocalPort, @Optional("iOSOnly")String webkitDebugProxyPort) throws Exception {
		PageDriver.setDateTime(utils.dateTime());
		PageDriver.setPlatform(platformName);
		PageDriver.setDeviceName(deviceName);
		URL url;
		InputStream inputStream = null;
		InputStream stringsis = null;
		Properties props = new Properties();
		AppiumDriver driver;
		
		String strFile = "logs" + File.separator + platformName + "_" + deviceName;
		File logFile = new File(strFile);
		if (!logFile.exists()) {
			logFile.mkdirs();
		}
		ThreadContext.put("ROUTINGKEY", strFile);
		utils.log().info("log path: " + strFile);
		
		try {
			props = new Properties();
			String propFileName = "config.properties";
			String xmlFileName = "strings/strings.xml";
		  
			utils.log().info("load " + propFileName);
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			props.load(inputStream);
			PageDriver.setProps(props);
		  
			utils.log().info("load " + xmlFileName);
			stringsis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
			System.out.print(stringsis);
			PageDriver.setStrings(utils.parseStringXML("C:\\SDET_Intermediate\\AndroidAppiumDemo\\src\\test\\resources\\strings\\strings.xml"));
		  
			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setCapability("platformName", platformName);
			desiredCapabilities.setCapability("deviceName", deviceName);
			desiredCapabilities.setCapability("udid", udid);
			url = URI.create(props.getProperty("appiumURL")).toURL();
			
			switch(platformName) {
				case "Android":
					desiredCapabilities.setCapability("automationName", props.getProperty("androidAutomationName"));	
					desiredCapabilities.setCapability("appPackage", props.getProperty("androidAppPackage"));
					desiredCapabilities.setCapability("appActivity", props.getProperty("androidAppActivity"));
					if(emulator.equalsIgnoreCase("true")) {
						desiredCapabilities.setCapability("avd", deviceName);
						desiredCapabilities.setCapability("avdLaunchTimeout", 120000);
					}
					desiredCapabilities.setCapability("systemPort", systemPort);
					desiredCapabilities.setCapability("chromeDriverPort", chromeDriverPort);
					String androidAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
								+ File.separator + "resources" + File.separator + "app" + File.separator + "Android.SauceLabs.Mobile.Sample.app.2.2.1.apk";
					utils.log().info("appUrl is" + androidAppUrl);
					desiredCapabilities.setCapability("app", androidAppUrl);
	
					driver = new AndroidDriver(url, desiredCapabilities);
					break;
				case "iOS":
					desiredCapabilities.setCapability("automationName", props.getProperty("iOSAutomationName"));
					String iOSAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
							+ File.separator + "resources" + File.separator + "app" + File.separator + "SwagLabsMobileApp.app";
					utils.log().info("appUrl is" + iOSAppUrl);
					desiredCapabilities.setCapability("bundleId", props.getProperty("iOSBundleId"));
					desiredCapabilities.setCapability("wdaLocalPort", wdaLocalPort);
					desiredCapabilities.setCapability("webkitDebugProxyPort", webkitDebugProxyPort);
					desiredCapabilities.setCapability("app", iOSAppUrl);
	
					driver = new IOSDriver(url, desiredCapabilities);
					break;
				default:
					throw new Exception("Invalid platform! - " + platformName);
			}
			PageDriver.setDriver(driver);
			utils.log().info("driver initialized: " + driver);
		} catch (Exception e) {
		  utils.log().fatal("driver initialization failure. ABORT!!!\n" + e.toString());
		  throw e;
		} finally {
			if(inputStream != null) {
				inputStream.close();
			}
			if(stringsis != null) {
				stringsis.close();
			}
		}
	}
  
	public void waitForVisibility(WebElement e){
		Wait<WebDriver> wait = new FluentWait<WebDriver>(PageDriver.getDriver())
				.withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(5))
				.ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.visibilityOf(e));
	}
  
	public void clear(WebElement e) {
		waitForVisibility(e);
		e.clear();
	}
  
	public void click(WebElement e) {
		waitForVisibility(e);
		e.click();
	}
  
	public void click(WebElement e, String msg) {
		waitForVisibility(e);
		utils.log().info(msg);
		ExtentReport.getTest().log(Status.INFO, msg);
		e.click();
	}
  
	public void sendKeys(WebElement e, String txt) {
		waitForVisibility(e);
		e.sendKeys(txt);
	}
  
	public void sendKeys(WebElement e, String txt, String msg) {
		waitForVisibility(e);
		utils.log().info(msg);
		ExtentReport.getTest().log(Status.INFO, msg);
		e.sendKeys(txt);
	}
  
	public String getAttribute(WebElement e, String attribute) {
		waitForVisibility(e);
		return e.getAttribute(attribute);
	}
  
	public String getText(WebElement e, String msg) {
		String txt = null;
		switch(PageDriver.getPlatform()) {
			case "Android":
				txt = getAttribute(e, "text");
				break;
			case "iOS":
				txt = getAttribute(e, "label");
				break;
		}
		utils.log().info(msg + txt);
		ExtentReport.getTest().log(Status.INFO, msg + txt);
		return txt;
	}
  
	public void closeApp(String appPackage){
		((JavascriptExecutor) PageDriver.getDriver()).executeScript("mobile: clearApp", ImmutableMap.of("appId",appPackage));
	}
	
	public void launchApp(String appPackage, String appActivity){
		PageDriver.getDriver().executeScript("mobile: startActivity", ImmutableMap.of("intent",appPackage+"/"+appActivity));
	}
  
	public WebElement scrollToElement() {	  
		return (WebElement) (PageDriver.getDriver()).findElement(By.partialLinkText(
				"new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
						+ "new UiSelector().description(\"test-Price\"));"));
	}
  
	public void iOSScrollToElement() {
		RemoteWebElement element = (RemoteWebElement)PageDriver.getDriver().findElement(By.name("test-ADD TO CART"));
		String elementID = element.getId();
		HashMap<String, String> scrollObject = new HashMap<String, String>();
		scrollObject.put("element", elementID);
		scrollObject.put("toVisible", "sdfnjksdnfkld");
		PageDriver.getDriver().executeScript("mobile:scroll", scrollObject);
	}

	@AfterTest (alwaysRun = true)
	public void afterTest() {
		if(PageDriver.getDriver() != null){
			PageDriver.getDriver().quit();
		}
	}
}
