package testCases;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
	Sheet excelSheet;

	@BeforeClass(alwaysRun = true)
	public void setup() {
		Constant.driver = openBrowser("IE");
		driver = Constant.driver;
		Constant.mapVarialbe = new HashMap<String, String>();
		Constant.currentTestCasePosition = new HashMap<String, Integer>();
	}

	@Test(dataProvider = "testCasePos")
	public void executeKeywordTest(String testcaseName, int pos) throws IOException {

		ReadObject object = new ReadObject();
		Properties allObjects = object.getObjectRepository();
		UIOperation operation = new UIOperation(driver);
		try {
			Sheet sheet = file.readExcel(Constant.excelFilePath, Constant.testCaseFileName, Constant.keyWordSheet);

			int lastRowNum = sheet.getLastRowNum() - sheet.getFirstRowNum();

			for (int i = pos + 1; i < lastRowNum + 1; i++) {

				Row row = sheet.getRow(i);
				Row preRow = sheet.getRow(i - 1);
				String actions = row.getCell(1).toString();
				String objectName = row.getCell(2).toString();
				String condition = row.getCell(3).toString();
				String value = row.getCell(4).toString();
				String variable = row.getCell(5).toString();

				if (preRow.getCell(0).toString().length() != 0) {
					System.out.println("\nTESTCASE :" + preRow.getCell(0).toString().toUpperCase() + " IS RUNNING\n");
				} else {
					System.out.println("\nSTEP: ");
				}

				try {
					if (row.getCell(0).toString().length() == 0) {
						System.out.println(actions + "----" + objectName + "----" + value + "----" + variable);
						if (actions.equals("IF")) {
							if (operation.processCondition(driver, condition) == Boolean.parseBoolean(value)) {
								for (int j : processIfElse("IF", pos)) {
									i = j;
									break;
								}
							} else {
								for (int j : processIfElse("ELSE", pos)) {
									i = j;
									break;
								}
							}
						}
						if (actions.equals("RUNTESTCASE")) {
							Constant.currentTestCasePosition.put(testcaseName, i + 2);
							for (Map.Entry<String, Integer> entry : testcasePos().entrySet()) {
								if (entry.getKey().equals(value)) {
									this.executeKeywordTest(entry.getKey(), entry.getValue());
								}
							}
						}
						operation.perform(allObjects, actions, objectName, value, variable);
					} else {
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	@DataProvider(name = "testCasePos")
	private Iterator<Object[]> getTestcase() throws IOException {

		Collection<Object[]> testcase = new ArrayList<Object[]>() {
			private static final long serialVersionUID = 1L;

			{
				try {
					excelSheet = file.readExcel(Constant.excelFilePath, Constant.testCaseFileName,
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

	private Map<String, Integer> testcasePos() {
		Map<String, Integer> testcasePos = new HashMap<String, Integer>();
		try {
			excelSheet = file.readExcel(Constant.excelFilePath, Constant.testCaseFileName, Constant.keyWordSheet);

			for (int i = 1; i <= excelSheet.getLastRowNum(); i++) {
				Cell testCaseName = excelSheet.getRow(i).getCell(0);
				if (!testCaseName.getStringCellValue().isEmpty()) {
					testcasePos.put(testCaseName.getStringCellValue(), i);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return testcasePos;

	}

	private ArrayList<Integer> processIfElse(String condition, int startPosition) {
		ArrayList<Integer> ifElseBlock = new ArrayList<Integer>();
		try {
			excelSheet = file.readExcel(Constant.excelFilePath, Constant.testCaseFileName, Constant.keyWordSheet);
			for (int i = startPosition + 1; i <= excelSheet.getLastRowNum(); i++) {
				Cell actionName = excelSheet.getRow(i).getCell(1);
				if (actionName.toString().equals(condition.toUpperCase())) {
					while (i < excelSheet.getLastRowNum()
							&& !excelSheet.getRow(i + 1).getCell(1).toString().equals("ELSE")) {
						ifElseBlock.add(i);
						i++;
					}
				} else if (actionName.toString().equals(condition.toUpperCase())) {
					while (i < excelSheet.getLastRowNum()
							&& !excelSheet.getRow(i + 1).getCell(1).toString().equals("ENDIF")) {
						ifElseBlock.add(i);
						i++;
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ifElseBlock;
	}

	@AfterClass(alwaysRun = true)
	public void setupAfterSuite() {
		// generateUIReport(tests);
		closeBrowser(driver);

	}
}
