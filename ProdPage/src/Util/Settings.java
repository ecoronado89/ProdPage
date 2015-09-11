package Util;



import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class Settings {
	
	public static WebDriver driver;
	public static EventFiringWebDriver eventFiringWebDriver;
	public static String mainWindowHandle;
	public static String page;
	public static List<String> productPages;
	public static boolean production; //false if it's on stage

	@BeforeTest
	public void testSetUp(){
		
		//Sets up the Chrome driver
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\extra-files\\chromedriver.exe");
		
		driver = new ChromeDriver(DesiredCapabilities.chrome());
		eventFiringWebDriver = new EventFiringWebDriver (driver);
		//Deletes all cookies from the browser
        driver.manage().deleteAllCookies();
        
	}
	
	@AfterMethod
	public void testShutDown(){
		
		//After the test, it has to shutdown the driver
		driver.close();
		
		String url = System.getProperty("user.dir")+"/test-output";
	    File file = new File(url);
	    
	    try{
	    	Desktop.getDesktop().open(file);
	    }catch(IOException e){
	    }
	    
	}
	

}
