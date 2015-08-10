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

public class TestingProductPage extends Util.Settings {

	@Test
	public void test() throws BiffException, IOException, FileNotFoundException{

		System.setOut(new PrintStream(new FileOutputStream("C:\\pdp_src\\output.txt")));
		
		Sheet sheet = HandleInput.readFile();
		if (sheet != null) {
			int rows = sheet.getRows();
			for (int i = 0; i < rows; i++) {
				page = sheet.getCell(0, i).getContents();
				if (page.equalsIgnoreCase("")) {
					Reporter.log("<br>END OF AUTOMATION");
					break;
				} else {
					driver.get("http://www.llbean.com/llb/shop/" + page);
					mainWindowHandle = driver.getWindowHandles().iterator()
							.next();
					
					Reporter.log("<br>********* Processing page: " + page
							+ " *********");
					

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

		// looks for Page Not Available image
		if (driver.getTitle().equalsIgnoreCase(notAvailTitle)) {
			available = false;
			Reporter.log("<br>Page is Not Available");
		}
		return available;
	}

	private boolean isProductAvailable() {
		Boolean prodAvailable = true;
		try {
			driver.findElement(By.cssSelector(Selector.PROD_AVAIL));
			Reporter.log("<br>Product is NOT available;");
			prodAvailable = false;

		} catch (NoSuchElementException n) {

		}
		return prodAvailable;
	}

	private boolean isSoldOut() {
		Boolean soldOut = true;
		try {
			WebElement priceCont = driver.findElement(By
					.cssSelector(Selector.ITM_PRICE));
			WebElement redPrice = priceCont.findElement(By
					.cssSelector(Selector.SOLD_OUT));
			if (redPrice.getText().equalsIgnoreCase("Sold Out")) {
				Reporter.log("<br>Page is Sold Out");
				soldOut = false;
			}

		} catch (NoSuchElementException n) {

		}
		return soldOut;
	}

	private boolean validateSizeChart() {
		Boolean sizeChart = false;
		try {
			driver.findElement(By.cssSelector(Selector.SCHART));
			sizeChart = true;
		} catch (NoSuchElementException n) {
			Reporter.log("<br>Size Chart Not available");
		}
		return sizeChart;

	}

	// Validate if Hero image is displayed
	private boolean verifyImage() {
		Boolean HImage = false;
		try {
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
