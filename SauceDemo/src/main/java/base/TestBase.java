package base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import pageObjects.LoginPage;
import utils.GenericMethods;
import utils.ScreenshotListener;
@Listeners(ScreenshotListener.class)
public class TestBase {
	WebDriver driver = null;
	GenericMethods genericMethodsObj = new GenericMethods();

	LoginPage loginPageObj;
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent = new ExtentReports();
	public ExtentTest logger = extent.createTest("loginTestPositive");
	public ExtentTest logger2 = extent.createTest("loginTestNegative");

	String sTarget = null;
    boolean bFlag = false;

	@BeforeSuite
	@Parameters({"browserName","url"})
	public void setUp(String browserName,String url) throws IOException{
		System.out.println("Inside @BeforeSuite setUp()");
		if(browserName.equalsIgnoreCase("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--remote-allow-origins=*");
			System.setProperty("webdriver.chrome.driver", "C:\\SDET_Intermediate\\chromedriver.exe");
			driver = new ChromeDriver(options);
		}

		else if(browserName.equalsIgnoreCase("edge")) {
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--remote-allow-origins=*");
			System.setProperty("webdriver.edge.driver", "C:\\SDET_Intermediate\\edgedriver.exe");
			driver = new EdgeDriver(options);
		}

		else if(browserName.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.firefox.driver", "C:\\SDET_Intermediate\\firefoxdriver.exe");
			driver = new FirefoxDriver();
		}
		PageDriver.setDriver(driver);
	    driver.manage().window().maximize();
	    driver.get(url);
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

		sparkReporter =  new ExtentSparkReporter(System.getProperty("user.dir")+File.separator+"reports"+File.separator+"SDETExtentReport.html");
		extent.attachReporter(sparkReporter);
		sparkReporter.config().setTheme(Theme.DARK);
		extent.setSystemInfo("HostName", "ssyed7");
		extent.setSystemInfo("Username", "root");
		sparkReporter.config().setDocumentTitle("Sauce Demo report");
		sparkReporter.config().setReportName("Results of SDET Automation");
		
		try{
			genericMethodsObj.assertPageTitle();
		    logger.log(Status.PASS, MarkupHelper.createLabel("Page title verified",ExtentColor.GREEN));
		} catch(AssertionError e) {
		    logger2.log(Status.FAIL, MarkupHelper.createLabel("Title page error",ExtentColor.RED));
		}
	}

	@BeforeMethod
	public void beforeMethodMethod() throws IOException {
		System.out.println("Inside @BeforeMethod beforeMethodMethod()");
	}

	@Test
	@Parameters({"username","password"})
	public void loginTest(String username, String password, ITestResult result) throws InterruptedException {
		System.out.println("Inside @Test loginTest()");
		genericMethodsObj.performLogin(username, password);
	    logger.log(Status.PASS, MarkupHelper.createLabel("Login Success",ExtentColor.GREEN));
	}


	@AfterMethod
	public void afterMethodMethod(ITestResult result) throws IOException {
		System.out.println("Inside @AfterMethod afterMethodMethod()");

		if (result.getStatus() == ITestResult.FAILURE && driver instanceof TakesScreenshot) {
			ScreenshotListener screenshotListener = new ScreenshotListener();
			screenshotListener.setDriver(driver);
		}
		if (result.getStatus() == ITestResult.FAILURE) {
			logger2.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+" - Testcase failed",ExtentColor.RED));
			logger2.log(Status.FAIL, MarkupHelper.createLabel(result.getThrowable()+" - Testcase failed",ExtentColor.RED));
			if (driver instanceof TakesScreenshot) {
				TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
				File Source = screenshotDriver.getScreenshotAs(OutputType.FILE);
				sTarget = result.getName() + ".png";
				try {
					Files.copy(Source.toPath(), new File(System.getProperty("user.dir")+"/reports/"+sTarget).toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			logger2.addScreenCaptureFromPath(sTarget);
		}
	}

	@AfterTest
	public void afterTestMethod() {
		System.out.println("Inside @AfterTest afterTestMethod()");
		try{
			genericMethodsObj.goToHomePage();
		    logger.log(Status.PASS, MarkupHelper.createLabel("Returned to Homepage - Logout success",ExtentColor.GREEN));
		} catch(AssertionError e) {
		    logger2.log(Status.FAIL, MarkupHelper.createLabel("Error in Login",ExtentColor.RED));
		}

	}

	@AfterSuite
	public void afterSuiteMethod() {
		System.out.println("Inside @AfterSuite afterSuiteMethod()");
		extent.flush();
		PageDriver.getDriver().quit();

	}



}