package utils;

import testBase.PageDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TestUtils {
	public static final long WAIT = 10;
	
	public HashMap<String, String> parseStringXML(InputStream file) throws Exception{
		HashMap<String, String> stringMap = new HashMap<String, String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		 
		Document document = builder.parse(file);
		document.getDocumentElement().normalize();
		NodeList nList = document.getElementsByTagName("string");
		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				stringMap.put(eElement.getAttribute("name"), eElement.getTextContent());
			}
		}
		return stringMap;
	}
	
	public String dateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public void log(String txt) {
		String msg = Thread.currentThread().threadId() + ":" + PageDriver.getPlatform() + ":" + PageDriver.getDeviceName() + ":"
				+ Thread.currentThread().getStackTrace()[2].getClassName() + ":" + txt;
		
		System.out.println(msg);
		
		String strFile = "logs" + File.separator + PageDriver.getPlatform() + "_" + PageDriver.getDeviceName() 
				+ File.separator + PageDriver.getDateTime();

		File logFile = new File(strFile);

		if (!logFile.exists()) {
			logFile.mkdirs();
		}
		
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(logFile + File.separator + "log.txt",true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    printWriter.println(msg);
	    printWriter.close();
	}

	public Logger log() {
		return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
	}

	public HashMap<String, String> parseStringXML(String string) {
		return null;
	}
	
}
