package defaultPackage;

import java.io.IOException;
import java.util.List;

import jxl.Sheet;
import jxl.read.biff.BiffException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/* Class where the tests are executed */
public class TestingProductPage extends Util.Settings {

	@Test
	public void test() throws BiffException, IOException, FileNotFoundException{

		System.setOut(new PrintStream(new FileOutputStream("C:\\pdp_src\\output.txt")));
		
		Sheet sheet = HandleInput.readFile();
		
		//Validates if there's a sheet
		if (sheet != null) {
			int rows = sheet.getRows();
			for (int i = 0; i < rows; i++) {
				//Gets the first product page
				page = sheet.getCell(0, i).getContents();
				if (page.equalsIgnoreCase("")) {
					Reporter.log("<br>END OF AUTOMATION");
					break;
				} else {
					//Gets the web page
					driver.get("http://www.llbean.com/llb/shop/" + page);
					mainWindowHandle = driver.getWindowHandles().iterator()
							.next();
					
					Reporter.log("<br>********* Processing page: " + page
							+ " *********");
					
					//Validates the page
					if (isPageAvailable() == true) {
						isSoldOut();
						isProductAvailable();
						validateSizeChart();
						validateBreadcrum();
						verifyImage();
					}
				}
			}

		} else {
			Reporter.log("Sheet of pages is null.");
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

	private boolean isSoldOut() {
		Boolean soldOut = true;
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
				soldOut = false;
			}
		} catch (NoSuchElementException n) {
			/*If the block goes to the exception, it means that the css selector is not present,
			* therefore, the product is not sold out */
		}
		return soldOut;
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

	// Validates if the hero image is being displayed
	private boolean verifyImage() {
		Boolean HImage = false;
		try {
			//Obtains the src of the hero image
			String heroImage = driver.findElement(
					By.xpath("id('backImageSjElement4_img')"))
					.getAttribute("src");

			try {
				if (heroImage.contains("img_not_avail")) {
					Reporter.log("<br>Hero image is broken");
					HImage = true;
				}

				} catch (NullPointerException e) {
					Reporter.log("<br>Source not found");
			}
			
			List<WebElement> AVimages = driver.findElements(By
					.xpath("id('ppAlternateViews')/div"));

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

	

	private boolean validateBreadcrum() {
		Boolean breadC = false;
		try {
			driver.findElement(By.cssSelector(Selector.BREADC));
			breadC = true;
		} catch (NoSuchElementException n) {
			Reporter.log("<br>Breadcrumbs Not available");
		}
		return breadC;

	}

}
