package defaultPackage;

import java.io.FileInputStream;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class HandleInput {

	
	//open read and return array with all pages
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
