package testCases;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import common.AbstractTest;
import common.Constant;
import excelExportAndFileIO.ReadExcelFile;
import operation.ReadObject;
import operation.UIOperation;

public class ExecuteTest extends AbstractTest {

	WebDriver driver;
	ReadExcelFile file = new ReadExcelFile();

	@BeforeClass(alwaysRun = true)
	public void setup() {
		driver = openBrowser("IE");
	}

	@Test(dataProvider = "testCasePos")
	public void testLogin(String testcaseName, int pos) throws IOException {

		ReadObject object = new ReadObject();
		Properties allObjects = object.getObjectRepository();
		UIOperation operation = new UIOperation(driver);
		try {
			Sheet sheet = file.readExcel(Constant.excelFilePath, Constant.testCaseFileName, Constant.keyWordSheet);

			int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();

			for (int i = pos + 1; i < rowCount + 1; i++) {

				Row row = sheet.getRow(i);
				Row preRow = sheet.getRow(i - 1);

				if (preRow.getCell(0).toString().length() != 0) {
					System.out.println("\nTESTCASE :" + preRow.getCell(0).toString().toUpperCase() + " IS RUNNING\n");
				} else {
					System.out.println("\nSTEP: ");
				}

				try {
					if (row.getCell(0).toString().length() == 0) {

						System.out.println(row.getCell(1).toString() + "----" + row.getCell(2).toString() + "----"
								+ row.getCell(3).toString() + "----" + row.getCell(5).toString());

						operation.perform(allObjects, row.getCell(1).toString(), row.getCell(2).toString(),
								row.getCell(3).toString(), row.getCell(4).toString(), row.getCell(5).toString());

					} else {
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@DataProvider(name = "testCasePos")
	private Iterator<Object[]> getTestcase() throws IOException {

		Collection<Object[]> testcase = new ArrayList<Object[]>() {
			private static final long serialVersionUID = 1L;

			{
				try {
					Sheet excelSheet = file.readExcel(Constant.excelFilePath, Constant.testCaseFileName,
							Constant.keyWordSheet);
					for (int i = 1; i <= excelSheet.getLastRowNum(); i++) {

						Cell testCaseName = excelSheet.getRow(i).getCell(0);

						if (!testCaseName.getStringCellValue().isEmpty()) {

							add(new Object[] { excelSheet.getRow(i).getCell(0).getStringCellValue(), i });

						}
					}

				} catch (FileNotFoundException e) {
					System.out.println("Could not read the Excel sheet");
					e.printStackTrace();
				}
			}
		};
		return testcase.iterator();
	}

	@AfterClass(alwaysRun = true)
	public void setupAfterSuite() {
		// generateUIReport(tests);
		closeBrowser(driver);

	}
}
