package defaultPackage;

import java.io.IOException;
import java.util.List;

import jxl.Sheet;
import jxl.read.biff.BiffException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class TestingProductPage extends Util.Settings {

	@Test
	public void test() throws BiffException, IOException {

		Sheet sheet = HandleInput.readFile();
		if (sheet != null) {
			int rows = sheet.getRows();
			for (int i = 0; i < rows; i++) {
				page = sheet.getCell(0, i).getContents();
				driver.get("http://www.llbean.com/llb/shop/" + page);
				mainWindowHandle = driver.getWindowHandles().iterator().next();

				System.out.println("\n********* Processing page: " + page
						+ " *********");

				if (isPageAvailable() == true) {
					isSoldOut();
					isProductAvailable();
					validateSizeChart();
					validateBreadcrum();
					verifyImage();

				}

			}

		} else {
			System.out.println("Sheet of pages is null.");
		}
	}

	private boolean isPageAvailable() {
		Boolean available = true;
		String notAvailTitle = "L.L.Bean: Page Not Available";

		// looks for Page Not Available image
		if (driver.getTitle().equalsIgnoreCase(notAvailTitle)) {
			available = false;
			System.err.println("Page is Not Available");
		}
		return available;
	}

	private boolean isProductAvailable() {
		Boolean prodAvailable = true;
		try {
			driver.findElement(By.cssSelector(Selector.PROD_AVAIL));
			System.err.println("Product is NOT available;");
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
				System.err.println("Page is Sold Out");
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
			System.err.println("Size Chart Not available");
		}
		return sizeChart;

	}

	// Validate if Hero image is displayed
	private static boolean verifyImage() {
		Boolean HImage = false;
		try {
			String heroImage = driver.findElement(
					By.xpath("//*[@id='backImageSjElement4_img']"))
					.getAttribute("src");

			try {
				if (heroImage.contains("img_not_avail")) {
					System.err.println("Hero image is broken");
					HImage = true;
				}

				validateAV();
			} catch (NullPointerException e) {
				System.err.print("Source not found");
			}
		} catch (NoSuchElementException n) {
			System.err.println("Image Not loaded");
		}
		return HImage;
	}

	// Validate if all AV are correct
	public static void validateAV() {
		try {
			List<WebElement> AVimages = driver.findElements(By
					.xpath("//*[@id='ppAlternateViews']/div"));

			for (int count = 0; count < AVimages.size(); count++) {
				int alt = count + 1;
				String AV = driver.findElement(
						By.xpath("//*[@id='ppAlternateViews']/div[" + alt
								+ "]/a/img")).getAttribute("src");
				if (AV.contains("IMG_not_avail_"))
					System.err.println("Alternate View " + alt
							+ " is not available");

			}
		} catch (NullPointerException e) {
			System.err.print("Product does not contain Alternate views");
		}

	}

	private boolean validateBreadcrum() {
		Boolean breadC = false;
		try {
			driver.findElement(By.cssSelector(Selector.BREADC));
			breadC = true;
		} catch (NoSuchElementException n) {
			System.err.println("Breadcrum Not available");
		}
		return breadC;

	}

}
