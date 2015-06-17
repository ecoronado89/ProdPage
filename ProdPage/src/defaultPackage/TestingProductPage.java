package defaultPackage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jxl.Sheet;
import jxl.read.biff.BiffException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.specification.RequestSpecification;


@SuppressWarnings("deprecation")
public class TestingProductPage {
	
	WebDriver driver;
	EventFiringWebDriver eventFiringWebDriver;
	String mainWindowHandle;
	String page;
	RequestSpecification requestSpecification;


	@BeforeTest
	public void testSetUp(){
		// create a driver
			
//		Internet Explorer		
//		File file = new File("C:/litofiles/Automation/Research/Selenium/IEDriverServer.exe");		
//		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
//		DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
//		dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);  //If IE fail to work, please remove this and remove enable protected mode for all the 4 zones from Internet options
//		driver = new InternetExplorerDriver(dc);

//		Firefox		
//	     driver = new FirefoxDriver();


//		Chrome
		File file = new File("C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		driver = new ChromeDriver(DesiredCapabilities.chrome());
		
		
		eventFiringWebDriver = new EventFiringWebDriver (driver);

//	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        
       
        
        
	}
	
	@AfterTest
	public void testShutDown(){
		//ShutDown selenium driver
		driver.close();
	}
	
	@Test
	public void test() throws BiffException, IOException{
		
		//String[] pages = {"74891","444444","31179", "83440"};
	//	Sheet sheet = HandleInput.readFile();
		//if (sheet != null){
		//	int rows = sheet.getRows();
		//	for (int i = 0; i < rows; i++) {
	      //  	page = sheet.getCell(0,i).getContents();
	        	driver.get("http://ecwebs01.llbean.com/llb/shop/"+36069);
	            mainWindowHandle = driver.getWindowHandles().iterator().next();
	            //valAllSwatches();
	            
	            if (isPageAvailable()&&isProductAvailable()&&validateSizeChart()&&verifyImage()){
	            	//isSoldOut();
	            	System.out.println("Processing page: "+page);
	            }
			//}
			
	//	}else{
		//	System.out.println("Sheet of pages is null.");
	//	}
	}
	
	private boolean isPageAvailable(){
		Boolean available = true;
		String notAvailTitle = "L.L.Bean: Page Not Available";
		
		//looks for Page Not Available image
		if (driver.getTitle().equalsIgnoreCase(notAvailTitle)){
			available = false;
			System.out.println("*********Page: "+page+" Not Available");
		}    
		return available;
	}
	
	private boolean isProductPage(){
		Boolean productPage = false;
		try{
			WebElement body = driver.findElement(By.cssSelector(Selector.APP));
			if(body != null){
				if (body.getTagName().equalsIgnoreCase("body")){
					productPage = true;
				}else{
					System.out.println("Page: "+page+" contains a tag for cssSelector "+Selector.APP+" but it is not the body.");
				}
			}else{
				System.out.println("Page:"+page+" doesn't contain a tag with cssSelector "+Selector.APP);
			}
		}catch (NoSuchElementException n){
			System.out.println("Element not found for Selector: "+ Selector.APP);
		}
		return productPage;
	}
	
	private boolean isProductAvailable() {
		Boolean prodAvailable = true;
		try{
			driver.findElement(By.cssSelector(Selector.PROD_AVAIL));
			System.out.println("*********Product at page: "+ page + " is NOT available;");			
	        prodAvailable = false;
	        
		}catch (NoSuchElementException n){
			//System.out.println("Product at page: "+ page + " is available;");
		}
		return prodAvailable;
	}
		
	
	private void valProductName(){
		String productName;
		
		WebElement hName = driver.findElement(By.cssSelector(Selector.NAME));
		if (hName != null){
			productName = hName.getText();
			System.out.println("Product Name "+productName);
		}
		
	}
	
	private boolean isSoldOut() {
		Boolean soldOut = true;
		try{
			WebElement priceCont = driver.findElement(By.cssSelector(Selector.ITM_PRICE));
			WebElement redPrice = priceCont.findElement(By.cssSelector(Selector.SOLD_OUT));
			if(redPrice.getText().equalsIgnoreCase("Sold Out")){
				System.out.println("Page: "+ page + " is Sold Out");
				soldOut = false;	
			}		
	        
		}catch (NoSuchElementException n){
			//System.out.println("Product at page: "+ page + " is available;");
		}
		return soldOut;
	}
	
	
	/*private void getTabs(){
		
		try{
			int tabCount = 0;
			WebElement tabCont = null; 
			tabCont = driver.findElement(By.cssSelector(Selector.TABS_UL));
			if (tabCont != null){
				List<WebElement> tabList = tabCont.findElements(By.cssSelector("a"));
				for(WebElement tab : tabList){
					System.out.println("Tab :"+tab.getText());
					tabCount++;
				}
			}
			if (tabCount>0){
	    		System.out.println("Product has more than 1 tab");
	    	}else{
	    		System.out.println("Product doesn't have tabs");
	    	}

		}
		
	}*/
	
	private void valAllSwatches(){
		try{
			List<WebElement> swatches = getListOfSwatches();
			if (swatches != null){
				for (WebElement swatch : swatches) {
					//validate image loaded
					String fileName = swatch.getAttribute("swatchfilename");
					String imageTag = swatch.getAttribute("title") + " - " + swatch.getAttribute("id");
					//System.out.println(fileName);
					if (fileName != null){
						if (isBrokenImageRepsonse(fileName)){
							System.out.println("Image broken!!! page#" + page + " IMG:" + imageTag + " FILENAME:" + fileName );
						}
					}
				}	
			}
		}catch (NoSuchElementException n){
			System.out.println("There was an error validating swatches for page: " + page);
		}
	}
	
	private List<WebElement> getListOfSwatches(){
		List<WebElement> swatches = null;
		try{
			WebElement ordeOptContainer = driver.findElement(By.cssSelector(Selector.ORDOPTCONT));
			swatches = ordeOptContainer.findElements(By.tagName(Selector.IMG));		
		}catch (NoSuchElementException n){
			System.out.println("Warning!!! Page #"+ page + " has no swatches");
		}
		return swatches;
	}
	
	
	private void validateItemPrice(){
		//Validate if the content shows Sold Out text
		WebElement itemPriceContainer = driver.findElement(By.className("ppItemPriceContainer"));
		if (itemPriceContainer != null){
			// if there is no elements with itemproc attribute it could be Sold Out?
			//get elements within the price container (price, sale price, text, etc)
			List<WebElement> prices = itemPriceContainer.findElements(By.xpath("./*[@itemprop]"));
			for (WebElement option : prices) {
			    System.out.println(String.format("Value is: %s", option.getAttribute("itemprop")));

			    /*if (option.getAttribute("value").equals(vaLue)) {
			        System.out.println("Pass");
			    } else {
			        System.out.println("fail");
			    */
			}
			/*String priceContent = itemPriceContainer.toString();
			if (priceContent != null && priceContent.contains("Sold Out")) {
				System.out.println("Sold Out");
			}else{
				System.out.println(priceContent);
			}*/
		}else{	
			System.out.println("No item price container present");
		}
	}
	
  private Boolean isBrokenImageRepsonse(String imgUrl) {
	    Boolean isBroken = false;
	  	try {
	  		if (imgUrl != null){
	  			HttpResponse response = new DefaultHttpClient().execute(new HttpPost("http:"+imgUrl));
	  			if (response.getStatusLine().getStatusCode() != 200)
	  				isBroken = true;
	  		}
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  	return isBroken;
	  }
		
	private Boolean isBrokenImage(String imageUrl){
        Boolean isBroken = false;
		//String script = "return (typeof arguments[0].naturalWidth!=\"undefined\" && arguments[0].naturalWidth>0)";
		String script = "return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0";
        Object imgStatus = eventFiringWebDriver.executeScript(script, "http:" + imageUrl);
        if (imgStatus.equals(true)){
        	isBroken = true;
        }
        return isBroken;
	}
	
	private boolean validateSizeChart(){
		Boolean sizeChart = false;
		try{
			driver.findElement(By.cssSelector(Selector.SCHART));
			sizeChart = true;
		} catch(NoSuchElementException n){
			System.err.println("Page: "+ page + " without Size Chart");
		}
		return sizeChart;
		
		}
	
	
	private boolean verifyImage() 
	{
		Boolean HImage = false;
		
		String heroImage = driver.findElement(By.xpath("id('backImageSjElement4_img')")).getAttribute("src");
		
		if(heroImage.contains("img_not_avail"))
		{
			System.err.println("Hero image is broken");
			validateAV();
		}
		else
			System.out.println("Hero image displayed");
			return HImage;
	}
	public void validateAV(){
		List<WebElement> AVimages = driver.findElements(By.xpath("//*[@id='ppAlternateViews']/div"));
				
	
		 for(int count = 0; count < AVimages.size(); count++)
		 {
			 int alt = count+1;
			 String AV = driver.findElement(By.xpath("//*[@id='ppAlternateViews']/div["+alt+"]/a/img")).getAttribute("src");
			 if(AV.contains("IMG_not_avail_"))
			 	 System.err.println("Alternate View "+alt+" is not available");
			 else 
				 System.out.println("Alternate View "+alt+" displayed");
			 
		 }
		
	}
	
	}

