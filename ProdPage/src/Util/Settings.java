package Util;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
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
	

}
