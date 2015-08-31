package defaultPackage;

import java.io.FileInputStream;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/*
 * Class that reads the list of products to check,
 * to add new products on the list, just go to "C:\\pdp_src\\pdplist.xls"
 * and add a new line for each product page with its number.
*/
public class HandleInput {
	
	/*
	 * Reads the excel file, and returns a Sheet
	 * (http://jexcelapi.sourceforge.net/resources/javadocs/2_6_10/docs/jxl/Sheet.html)
	 */
	public static Sheet readFile()throws IOException, BiffException{
		try{
			FileInputStream fi=new FileInputStream("C:\\pdp_src\\pdplist.xls");
			Workbook workbook = Workbook.getWorkbook(fi);
			Sheet sheet = workbook.getSheet("NewProducts");
			return sheet;
			
		}catch (IOException | BiffException ioe){
			System.out.println(ioe.getMessage());
			throw ioe;		 
		}
	}
}
