package defaultPackage;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/* Class where the tests are executed */
public class TestingProductPage extends Util.Settings {
	
	@Test
	public void test() throws IOException, FileNotFoundException{
		
		System.setOut(new PrintStream(new FileOutputStream(System.getProperty("user.dir")+"\\extra-files\\output.txt")));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		//Sets the url to production or stage accordingly
		String url = "http://"+ (production?"www":"ecwebs01") + ".llbean.com/llb/shop/";
		
		Reporter.log("<br><b>Processing from: " + url + " </b><br>");
		
		for (String pageNumber : productPages){
			//Gets the web page
			driver.get(url + pageNumber);
			mainWindowHandle = driver.getWindowHandles().iterator()
					.next();
			
			Reporter.log("<br>********* Processing page: " + pageNumber
					+ " *********");
			
			String result = (String) js.executeScript("return llJSP");
			System.out.println(result);
			
			//Validates the page
			if (isPageAvailable() == true) {
				
				inStock();
				isProductAvailable();
				validateSizeChart();
				validateBreadcrum();
				verifyImage();
				validateCopyExist();
				
			}
			
			Reporter.log("<br>********* Page: " + pageNumber
					+ " completed *****");
			
		}

		Reporter.log("<br><br>END OF AUTOMATION");
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

	private boolean isProductAvailable() {
		Boolean prodAvailable = true;
		try {
			//Validates if there's a css selector with tag .ppItemUnavailable
			driver.findElement(By.cssSelector(Selector.PROD_AVAIL));
			Reporter.log("<p style=\"color:red\">Product is not available</p>");
			prodAvailable = false;

		} catch (NoSuchElementException n) {
			/*If the block goes to the exception, it means that the css selector is not present,
			* therefore, the product is available */
		}
		return prodAvailable;
	}

	private boolean inStock() {
		
		Boolean inStock = true;
		try {
			//Gets the PriceContainer
			WebElement priceCont = driver.findElement(By
					.cssSelector(Selector.ITM_PRICE));
			//Gets the the selector that indicates that the product is sold out.
			WebElement redPrice = priceCont.findElement(By
					.cssSelector(Selector.SOLD_OUT));
			//Validates if the css selector is present because the product is sold out
			String price = redPrice.getText();
			price = price.replaceAll("\\s", "");
			price = price.toLowerCase();
			System.out.println(price);
			if (price.equals("soldout")) {
				Reporter.log("<p style=\"color:red\">Product is Sold Out</p>");
				inStock = false;
			}
		} catch (NoSuchElementException n) {
			//If the block goes to the exception, it means that the css selector is not present,
			// therefore, the product is not sold out 
		}
	
		return inStock;
	}

	//Validates if the size chart is present
	private boolean validateSizeChart() {
		Boolean sizeChart = false;
		try {
			driver.findElement(By.cssSelector(Selector.SCHART));
			sizeChart = true;
		} catch (NoSuchElementException n) {
			/*If the block goes to the exception, it means that the css selector is not present,
			* therefore, the size chart is not present */
			Reporter.log("<p style=\"color:red\">Size Chart not available</p>");
		}
		return sizeChart;

	}
	
	// Validates if the hero image and the alternate views are being displayed
	private boolean verifyImage() {
		Boolean HImage = true;
		try {
			//Obtains the src of the hero image
			String heroImage = driver.findElement(
					By.xpath("id('backImageSjElement4_img')"))
					.getAttribute("src");

			try {
				//If the hero image link is broken
				if (heroImage.contains("img_not_avail")) {
					Reporter.log("<p style=\"color:red\">Hero image is broken</p>");
					HImage = false;
				}
			} catch (NullPointerException e) {
				Reporter.log("<p style=\"color:red\">Source not found</p>");
			}
			
			//Obtains the alternate views
			List<WebElement> AVimages = driver.findElements(By
					.xpath("id('ppAlternateViews')/div"));

			/* Goes through the list of alternate views, checking if
			 * any image is not available, then adding it to the reporter */
			for (int count = 0; count < AVimages.size(); count++) {
				int alt = count + 1;
				String AV = driver.findElement(
						By.xpath("id('ppAlternateViews')/div[" + alt
								+ "]/a/img")).getAttribute("src");
				if (AV.contains("IMG_not_avail_"))
					Reporter.log("<p style=\"color:red\">Alternate View " + alt
							+ " is not available</p>");
			}
		} catch (NoSuchElementException n) {
			Reporter.log("<p style=\"color:red\">Image Not loaded</p>");
		}
		return HImage;
	}

	private boolean validateCopyExist() { 
        Boolean copy = false; 
        try { 
                driver.findElement(By.cssSelector(Selector.COPY)); 
                copy = true; 
        } catch (NoSuchElementException n) { 
                Reporter.log("<p style=\"color:red\">Copy not found</p>");
        } 
        return copy; 
    }
	
	// Validates if the breadcrumbs are present 
	private boolean validateBreadcrum() {
		Boolean breadC = false;
		try {
			driver.findElement(By.cssSelector(Selector.BREADC));
			breadC = true;
		} catch (NoSuchElementException n) {
			/*If the block goes to the exception, it means that the css selector is not present,
			* therefore, the breadcrumbs are not present */
			Reporter.log("<p style=\"color:red\">Breadcrumbs Not available</p>");
		}
		return breadC;

	}

}
