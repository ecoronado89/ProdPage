package defaultPackage;

import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URI;

/* Class where the tests are executed */
public class TestingProductPage extends Util.Settings {

	@Test
	public void test() throws IOException, FileNotFoundException{

		System.setOut(new PrintStream(new FileOutputStream(System.getProperty("user.dir")+"\\extra-files\\output.txt")));
		
		String url = "http://"+ (production?"www":"ecwebs01") + ".llbean.com/llb/shop/";
		Reporter.log("<br>**** Processing from: " + url
				+ " ******");
		
		int total = productPages.size();
		int position = 1;
		
		for (String pageNumber : productPages){
			
			//Gets the web page
			driver.get(url + pageNumber);
			mainWindowHandle = driver.getWindowHandles().iterator()
					.next();
			
			Reporter.log("<br>********* Processing page: " + pageNumber
					+ " *********");
			
			//Validates the page
			if (isPageAvailable() == true) {
				
				inStock();
				isProductAvailable();
				validateSizeChart();
				validateBreadcrum();
				verifyImage();
				
				Reporter.log("<br>********* Page: " + pageNumber
						+ " processed *****");
			}
			++position;
			
		}

		Reporter.log("<br>END OF AUTOMATION");
		driver.close();
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showResults();
	}
	
	private void showResults(){
		String url = System.getProperty("user.dir")+"/test-output/index.html";
	    File htmlFile = new File(url);
	    try{
	    	Desktop.getDesktop().browse(htmlFile.toURI());
	    }catch(IOException e){
	    	System.err.println(e.getMessage());
	    }
	}

	private boolean isPageAvailable() {
		Boolean available = true;
		String notAvailTitle = "L.L.Bean: Page Not Available";

		// looks if the page number is showing the Page Not Available title
		if (driver.getTitle().equalsIgnoreCase(notAvailTitle)) {
			available = false;
			Reporter.log("<br>Page is Not Available");
		}
		return available;
	}

	private boolean isProductAvailable() {
		Boolean prodAvailable = true;
		try {
			//Validates if there's a css selector with tag .ppItemUnavailable
			driver.findElement(By.cssSelector(Selector.PROD_AVAIL));
			Reporter.log("<br>Product is NOT available;");
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
			if (redPrice.getText().equalsIgnoreCase("Sold Out")) {
				Reporter.log("<br>Page is Sold Out");
				inStock = false;
			}
		} catch (NoSuchElementException n) {
			/*If the block goes to the exception, it means that the css selector is not present,
			* therefore, the product is not sold out */
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
			Reporter.log("<br>Size Chart Not available");
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
					Reporter.log("<br>Hero image is broken");
					HImage = false;
				}
			} catch (NullPointerException e) {
				Reporter.log("<br>Source not found");
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
					Reporter.log("<br>Alternate View " + alt
							+ " is not available");
			}
		} catch (NoSuchElementException n) {
			Reporter.log("<br>Image Not loaded");
		}
		return HImage;
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
			Reporter.log("<br>Breadcrumbs Not available");
		}
		return breadC;

	}

}
