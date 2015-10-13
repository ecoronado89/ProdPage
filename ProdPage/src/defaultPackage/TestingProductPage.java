package defaultPackage;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.openqa.selenium.Cookie;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

/* Class where the tests are executed */
public class TestingProductPage extends Util.Settings {
	
	@Test
	public void test() throws IOException, FileNotFoundException{
		
		//JavascriptExecutor js = (JavascriptExecutor) driver;
		
		//Sets the url to production or stage accordingly
		String url = "http://"+ (production?"www":"ecwebs01") + ".llbean.com/llb/shop/";
		
		Reporter.log("<br><b>Processing " + productPages.size() +" pages from: " + url + " </b><br>");
		
		//Gets the HP to set the cookies
		driver.get(url+0);		
		//Set any needed cookies (has to be after a get)
		setCookies();
		
		for (String pageNumber : productPages){
			
			//Gets the web page
			driver.get(url + pageNumber);
			
			mainWindowHandle = driver.getWindowHandles().iterator()
					.next();
			
			Reporter.log("<br>********* Processing page: " + pageNumber
					+ " *********");
			
			//String result = (String) js.executeScript("return llJSP");
			//System.out.println(result);
			
			//Validates the page
			if (isPageAvailable() == true) {
				
				NewPDP newPDP = new NewPDP();
				newPDP.inStock();
				newPDP.isProductAvailable();
				newPDP.validateSizeChart();
				newPDP.validateBreadcrum();
				newPDP.verifyImage(pageNumber);
				newPDP.validateCopyExist();
				
			}
			
			Reporter.log("<br>********* Page: " + pageNumber
					+ " completed *****");
			
		}

		Reporter.log("<br><br>END OF AUTOMATION");
	}
	
	private void setCookies(){
		/* Sets the LLBSS cookie that changes
		 * from the old PDP to the new PDP
		 * A = old. B= new. */
		System.out.println(llbssCookieValue);
		try{
			Cookie newPDP = new Cookie("LLBSS",llbssCookieValue);
			driver.manage().addCookie(newPDP);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Couldn't set cookie", "Cookie not set", JOptionPane.ERROR_MESSAGE);
			System.out.println(e);
		}
	}
	
	
	private boolean isPageAvailable() {
		Boolean available = true;
		String notAvailTitle = "L.L.Bean: Page Not Available";

		// looks if the page number is showing the Page Not Available title
		if (driver.getTitle().equalsIgnoreCase(notAvailTitle)) {
			available = false;
			Reporter.log("<p style=\"color:red\"> Page is Not Available</p>");
		}
		return available;
	}

}
