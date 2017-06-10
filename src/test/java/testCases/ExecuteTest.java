package testCases;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import junit.framework.Assert;
import operation.ReadObject;
import operation.UIOperation;

public class ExecuteTest extends AbstractTest {

	private WebDriver driver;
	ReadExcelFile file = new ReadExcelFile();
	public static String testcaseName;
	Sheet excelSheet;
	Boolean conditionIfElse = false;
	Boolean runAble = false;
	Map<String, Integer> testcasePos = new LinkedHashMap<String, Integer>();

	@BeforeClass(alwaysRun = true)
	public void setup() {
		Constant.driver = openBrowser("IE");
		driver = Constant.driver;
		Constant.mapVarialbe = new HashMap<String, String>();
		testcasePos();
	}

	@Test(dataProvider = "testCasePos")
	public void executeKeywordTest(String testcaseName, int startPos) {
		ExecuteTest.testcaseName = testcaseName;
		checkRun(testcaseName, runAble);
		try {
			UIOperation operation = new UIOperation(driver);
			ReadObject object = new ReadObject();
			Properties allObjects = object.getObjectRepository();
			excelSheet = file.keywordSheet();
			int lastRowNum = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
			for (int i = startPos + 1; i <= lastRowNum; i++) {
				Row row = excelSheet.getRow(i);
				String actions = row.getCell(1).toString();
				String objectName = row.getCell(2).toString();
				String condition = row.getCell(3).toString();
				String value = row.getCell(4).toString();
				String variable = row.getCell(5).toString();
				if (row.getCell(0).toString().length() == 0) {
					System.out.println("\nSTEP: ");
					System.out.println(actions + "----" + objectName + "----" + value + "----" + variable);
					if (actions.equals("IF") || actions.equals("ELSE")) {
						i = processIfElse(driver, condition, value, i, actions);
					}
					if (actions.equals("RUNTESTCASE")) {
						runtestCase(i, value);
					}
					operation.perform(allObjects, actions, objectName, value, variable);
				} else {
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkRun(String testcaseName, Boolean run) {

		for (Map.Entry<String, Integer> entry : testcasePos.entrySet()) {
			if (entry.getKey().equals(testcaseName)) {
				System.out.println("\nTESTCASE :" + testcaseName.toUpperCase() + " IS RUNNING\n");
				Assert.fail("Test Bug");
				run = true;
				break;
			}
		}
		if (!run) {
			System.out.println("\nTESTCASE :" + testcaseName.toUpperCase() + " IS SKIPED\n");
			// throw new SkipException("\nTESTCASE :" +
			// testcaseName.toUpperCase() + " IS SKIPED\n");
			Assert.fail("Test Bug");
		}
	}

	@DataProvider(name = "testCasePos")
	private Iterator<Object[]> getTestcase() throws IOException {
		Collection<Object[]> testcase = new ArrayList<Object[]>() {
			private static final long serialVersionUID = 1L;
			{
				try {
					excelSheet = file.keywordSheet();
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

	private void testcasePos() {
		try {
			excelSheet = file.keywordSheet();
			for (int i = 1; i <= excelSheet.getLastRowNum(); i++) {
				String testCaseName = excelSheet.getRow(i).getCell(0).toString();
				if (!testCaseName.isEmpty()) {
					testcasePos.put(testCaseName, i);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private int processIfElse(WebDriver driver, String condition, String value, int pos, String actions) {
		int currentPos = 0;

		try {
			excelSheet = file.keywordSheet();
			for (int i = pos; i <= excelSheet.getLastRowNum(); i++) {
				if (actions.equals("IF")) {
					if (UIOperation.processCondition(driver, condition) == Boolean.parseBoolean(value)) {
						conditionIfElse = true;
						currentPos = i;
						break;
					} else {
						while (!excelSheet.getRow(i + 1).getCell(1).toString().equals("ELSE")) {
							i++;
						}
						currentPos = i;
						break;
					}
				}
				if (actions.equals("ELSE")) {
					if (conditionIfElse) {
						while (!excelSheet.getRow(i + 1).getCell(1).toString().equals("ENDIF")) {
							i++;
						}
						currentPos = i;
						break;
					}
					currentPos = i;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return currentPos;
	}

	private void runtestCase(int pos, String testcaseToRun) throws IOException {
		try {
			excelSheet = file.keywordSheet();
			for (int i = pos; i <= excelSheet.getLastRowNum(); i++) {
				String actions = excelSheet.getRow(i).getCell(1).toString();
				if (actions.equals("RUNTESTCASE")) {
					for (Map.Entry<String, Integer> entry : testcasePos.entrySet()) {
						if (entry.getKey().equals(testcaseToRun)) {
							this.executeKeywordTest(entry.getKey(), entry.getValue());
						}
					}
					testcasePos.remove(testcaseToRun);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getTestcaseName() {
		return testcaseName;
	}

	@AfterClass(alwaysRun = true)
	public void setupAfterSuite() {
		// generateUIReport(tests);
		closeBrowser(driver);

	}
}
