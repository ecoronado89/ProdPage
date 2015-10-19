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
		
		//Gets the HP to set the cookies
		driver.get(url+0);		
		//Set any needed cookies (has to be after a get)
		setCookies();
		
		//Checks if the cookie is set to evaluate new or old PDP
		PDP pdp = (llbssCookieValue.equals("A"))? new OldPDP() : new NewPDP();
		
		Reporter.log("<br><b>Processing " + productPages.size() +" pages from: " + url + " with " + pdp.getPDPType() +" </b><br>");
		
		for (String pageNumber : productPages){
			
			//Gets the web page
			driver.get(url + pageNumber);
			
			//In case a pop-in interrupter appears, it gets ignored
			mainWindowHandle = driver.getWindowHandles().iterator()
					.next();
			
			Reporter.log("<br>********* Processing page: " + pageNumber
					+ " *********");
			
			//String result = (String) js.executeScript("return llJSP");
			
			//Validates the page
			if (isPageAvailable() == true) {
				
				pdp.inStock();
				pdp.isProductAvailable();
				pdp.validateSizeChart();
				pdp.validateBreadcrum();
				pdp.verifyImage(pageNumber);
				pdp.validateCopyExist();
				
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
		
		/*If cookie is set to NA, it means that no cookie was forced, uses the one
		  assigned by the page */
		if(llbssCookieValue.equals("NA")){
			llbssCookieValue = ""+driver.manage().getCookieNamed("LLBSS").getValue();
		}else{
			try{
				Cookie cookie = new Cookie("LLBSS",llbssCookieValue);
				driver.manage().addCookie(cookie);
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Couldn't set cookie", "Cookie not set", JOptionPane.ERROR_MESSAGE);
				System.out.println(e);
			}
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
