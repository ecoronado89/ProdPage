package Util;



import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class Settings {
	
	public static WebDriver driver;
	public static EventFiringWebDriver eventFiringWebDriver;
	public static String mainWindowHandle;
	public static String page;
	public static List<String> productPages;


	@BeforeTest
	public void testSetUp(){
		
		//Sets up the Chrome driver
		String s = System.setProperty("webdriver.chrome.driver", "C:/Users/anunez/AppData/Local/Google/Chrome/Application/chromedriver.exe");
		driver = new ChromeDriver(DesiredCapabilities.chrome());
		eventFiringWebDriver = new EventFiringWebDriver (driver);
		//Deletes all cookies from the browser
        driver.manage().deleteAllCookies();
	}
	
	@AfterTest
	public void testShutDown(){
		//After the test, it has to shutdown the driver
		System.out.println("Shutting down");
		driver.close();
	}
	

}
