package testBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.appium.java_client.AppiumDriver;

public class PageDriver {
	protected static ThreadLocal <AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
	protected static ThreadLocal <Properties> props = new ThreadLocal<Properties>();
	protected static ThreadLocal <HashMap<String, String>> strings = new ThreadLocal<HashMap<String, String>>();
	protected static ThreadLocal <String> platform = new ThreadLocal<String>();
	protected static ThreadLocal <String> dateTime = new ThreadLocal<String>();
	protected static ThreadLocal <String> deviceName = new ThreadLocal<String>();
	
	static JsonNode jsonNode;
	static JsonNode resourceNode;
	JsonNode invalidPassword;
	JsonNode validUser;
	

	public static AppiumDriver getDriver() {
		  return driver.get();
	  }
	  
	  public static void setDriver(AppiumDriver driver2) {
		  driver.set(driver2);
	  }
	  
	  public static Properties getProps() {
		  return props.get();
	  }
	  
	  public static void setProps(Properties props2) {
		  props.set(props2);
	  }
	  
	  public static HashMap<String, String> getStrings() {
		  return strings.get();
	  }
	  
	  public static String getStrings(String str) throws Exception{
		  String sReturn;
		  try {
			  String dataFileName = "C:/SDET_Intermediate/AndroidAppiumDemo/src/test/resources/strings/strings.json";
			  File file = new File(dataFileName);
			  InputStream inputStream = new FileInputStream(file);
			  InputStreamReader reader = new InputStreamReader(inputStream);
			  ObjectMapper objectMapper = new ObjectMapper();
			  jsonNode = objectMapper.readTree(reader);
			  resourceNode = jsonNode.get("resources");
			  sReturn = resourceNode.get(str).asText();
		  } catch(Exception e) {
			  e.printStackTrace();
			  throw e;
		  }
		return sReturn;
	  }
	  
	  public static void setStrings(HashMap<String, String> strings2) {
		  strings.set(strings2);
	  }
	  
	  public static String getPlatform() {
		  return platform.get();
	  }
	  
	  public static void setPlatform(String platform2) {
		  platform.set(platform2);
	  }
	  
	  public static String getDateTime() {
		  return dateTime.get();
	  }
	  
	  public static void setDateTime(String dateTime2) {
		  dateTime.set(dateTime2);
	  }
	  
	  public static String getDeviceName() {
		  return deviceName.get();
	  }
	  
	  public static void setDeviceName(String deviceName2) {
		  deviceName.set(deviceName2);
	  }
}
