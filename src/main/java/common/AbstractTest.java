package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;

import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;

public class AbstractTest {
	protected final Logger log;
	protected WebDriver driver;
	protected AutomationControl control = new AutomationControl();
	private static HSSFWorkbook excelBook;
	private static HSSFSheet excelSheet;
	private static HSSFCell cell;

	protected boolean verifyTrue(boolean condition, boolean halt) {
		boolean pass = true;
		if (halt == false) {
			try {
				Assert.assertTrue(condition);
				log.info("==Verify condition is PASSED==");
			} catch (Throwable e) {
				pass = false;
				VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
				Reporter.getCurrentTestResult().setThrowable(e);
				log.info("==Verify condition is FAILED==");
			}
		} else {
			Assert.assertTrue(condition);
		}
		return pass;
	}

	protected boolean verifyTrue(boolean condition) {
		return verifyTrue(condition, false);
	}

	protected boolean verifyFalse(boolean condition, boolean halt) {
		boolean pass = true;
		if (halt == false) {
			try {
				Assert.assertFalse(condition);
				log.info("==Verify condition is PASSED==");
			} catch (Throwable e) {
				pass = false;
				VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
				Reporter.getCurrentTestResult().setThrowable(e);
				log.info("==Verify condition is FAILED==");
			}
		} else {
			Assert.assertFalse(condition);
		}
		return pass;

	}

	protected boolean verifyFalse(boolean condition) {
		return verifyFalse(condition, false);
	}

	protected boolean verifyEquals(Object actual, Object expected, boolean halt) {
		boolean pass = true;
		if (halt == false) {
			try {
				Assert.assertEquals(actual, expected);
				log.info("==Verify condition is PASSED==");
			} catch (Throwable e) {
				pass = false;
				VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
				Reporter.getCurrentTestResult().setThrowable(e);
				log.info("==Verify condition is FAILED==");
			}
		} else {
			Assert.assertEquals(actual, expected);
		}
		return pass;
	}

	protected boolean verifyEquals(Object actual, Object expected) {
		return verifyEquals(actual, expected, false);
	}

	protected void generateUIReport(List<GalenTestInfo> tests) {
		try {
			HtmlReportBuilder reportBuilder = new HtmlReportBuilder();
			// // Create a report under /target folder based on tests list
			reportBuilder.build(tests, "target/UITest");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	protected void refreshBrowser(WebDriver driver) {
		driver.navigate().refresh();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public WebDriver openBrowser(String browser) {
		BrowserManager br = new BrowserManager();
		WebDriver driver = br.openBrowser(browser);
		if (driver.toString().toLowerCase().contains("chrome") || driver.toString().toLowerCase().contains("firefox")
				|| driver.toString().toLowerCase().contains("internetexplorer")) {
			driver.manage().window().maximize();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.focus();");
		}
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver remoteBrowser(String config_file, String environment, String url) throws Exception {
		BrowserManager br = new BrowserManager();
		WebDriver driver = br.remoteBrowser(config_file, environment, url);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		return driver;
	}

	protected void closeBrowser(WebDriver driver) {
		try {
			driver.quit();
			System.gc();
			if (driver.toString().toLowerCase().contains("chrome")) {
				String cmd = "taskkill /IM chromedriver.exe /F";
				Process process = Runtime.getRuntime().exec(cmd);
				process.waitFor();
			}
			if (driver.toString().toLowerCase().contains("internetexplorer")) {
				String cmd = "taskkill /IM IEDriverServer.exe /F";
				Process process = Runtime.getRuntime().exec(cmd);
				process.waitFor();
			}
			if (driver.toString().toLowerCase().contains("firefox")) {
				String cmd = "taskkill /IM geckodriver.exe /F";
				Process process = Runtime.getRuntime().exec(cmd);
				process.waitFor();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	protected String getUniqueName() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	protected String getUniqueNumber() {
		Random rand = new Random();
		int number = rand.nextInt(90000000) + 1;
		String numberString = Integer.toString(number);
		return numberString;
	}

	public void clearCookie(WebDriver driver) {
		driver.manage().deleteAllCookies();
	}

	protected AbstractTest() {
		log = Logger.getLogger(getClass());
	}

	public String takeScreenshot() {
		File src = ((TakesScreenshot) Constant.driver).getScreenshotAs(OutputType.FILE);
		String destFileLoctn = "./Screenshots/" + System.currentTimeMillis() + ".png";
		try {
			FileUtils.copyFile(src, new File(destFileLoctn));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(destFileLoctn);
		return destFileLoctn;
	}

	public static String getRandomString(String[] array) {
		int rnd = new Random().nextInt(array.length);
		return array[rnd];
	}

	public static String RandomEmail() {
		Random ran = new Random();
		int a = ran.nextInt(99999) + 1;
		return Constant.email + a + "@gmail.com";
	}

	public static String getRandomString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(getRandomChar());
		}
		return sb.toString();
	}

	public static char getRandomChar() {
		int rnd = (int) (Math.random() * 52); // or use Random or whatever
		char base = (rnd < 26) ? 'A' : 'a';
		return (char) (base + rnd % 26);
	}

	public static String isColorConvert(WebDriver driver, String locator) {
		String color = driver.findElement(By.xpath(locator)).getCssValue("color");
		String[] hexValue = color.replace("rgba(", "").replace(")", "").split(",");
		int hexValue1 = Integer.parseInt(hexValue[0]);
		hexValue[1] = hexValue[1].trim();
		int hexValue2 = Integer.parseInt(hexValue[1]);
		hexValue[2] = hexValue[2].trim();
		int hexValue3 = Integer.parseInt(hexValue[2]);
		String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
		return actualColor;
	}

	/**
	 * Open the Excel file
	 * 
	 * @param sheetName
	 * @throws Exception
	 */
	public void createExcelFile(String sheetName) {
		try {
			FileInputStream file = new FileInputStream(new File("Report.xls"));
			excelBook = new HSSFWorkbook(file);
			excelSheet = excelBook.createSheet(sheetName);
			System.out.println("Create Excel File");
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	/**
	 * Get data in excel file
	 * 
	 * @param rowNum
	 * @param colNum
	 * @return
	 */
	public String getCellData(int rowNum, int colNum) {
		try {
			cell = excelSheet.getRow(rowNum).getCell(colNum);
			String CellData = cell.getStringCellValue();
			return CellData;
		} catch (Exception e) {
			return "";
		}
	}

}
