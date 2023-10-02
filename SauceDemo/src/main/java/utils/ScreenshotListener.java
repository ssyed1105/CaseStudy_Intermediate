package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import base.PageDriver;
public class ScreenshotListener extends TestListenerAdapter {
	private WebDriver driver;
	String Target;

	public ScreenshotListener() {
		//default constructor
	}
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	@Override
	public void onTestFailure(ITestResult result) {
		this.driver = PageDriver.getDriver();
		System.out.println("inside test failed");
		System.out.println(System.getProperty("user.dir"));
		captueScreenshot(result);
	}

	private void captueScreenshot(ITestResult result) {
		this.driver = PageDriver.getDriver();
		if (driver instanceof TakesScreenshot) {
			TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
			File Source = screenshotDriver.getScreenshotAs(OutputType.FILE);
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

			Target = result.getName() + timeStamp + ".png";

			try {
				Files.copy(Source.toPath(), new File(System.getProperty("user.dir")+"/reports/"+Target).toPath(), StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Screenshot captured for test: "+result.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
