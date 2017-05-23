package testCases;

import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import common.AbstractTest;
import excelExportAndFileIO.ReadExcelFile;
import operation.ReadObject;
import operation.UIOperation;

public class ExecuteTest extends AbstractTest {
	WebDriver driver;
	@Test
	public void testLogin() throws Exception {
		
		//Browser define
		driver = openBrowser("IE");
		ReadExcelFile file = new ReadExcelFile();
		ReadObject object = new ReadObject();
		Properties allObjects = object.getObjectRepository();
		UIOperation operation = new UIOperation(driver);
		
		// Read keyword sheet
		Sheet sheet = file.readExcel(System.getProperty("user.dir") + "\\", "TestCase.xlsx", "KeywordFramework");
		
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			
			// Loop over all the rows
			Row row = sheet.getRow(i);
			
			// Check if the first cell contain a value, if yes, That means it is
			// the new testcase name
			try{
			if (row.getCell(0).toString().length() == 0) {
				// Print testcase detail on console
				System.out.println(row.getCell(1).toString() + "----" + row.getCell(2).toString() + "----"
						+ row.getCell(3).toString() + "----" + row.getCell(4).toString()+ "----" + row.getCell(5).toString());
				
				// Call perform function to perform operation on UI
				operation.perform(allObjects, row.getCell(1).toString(), row.getCell(2).toString(),
						row.getCell(3).toString(), row.getCell(4).toString(), row.getCell(5).toString());
			} else {
				
				// Print the new testcase name when it started
				System.out.println("\nNew Testcase -->" + row.getCell(0).toString() + " Started");
			}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	@AfterClass(alwaysRun = true)
	public void setupAfterSuite() {
//		generateUIReport(tests);
		closeBrowser(driver);

	}
}
