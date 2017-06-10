package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExcelReportListener implements IInvokedMethodListener, ITestListener {
	Map<String, ArrayList<String>> allDataAfterTest = new HashMap<String, ArrayList<String>>();
	private static HSSFWorkbook excelBook;
	private static HSSFSheet excelSheet;
	private static HSSFCell testCaseName, platformCellValue, resultCellValue, exceptionCell, timeToRunCell;

	private static final Log log = LogFactory.getLog(ExcelReportListener.class);

	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		setCellData(context);
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult result) {
		// TODO Auto-generated method stub
		addDataAfterTest(method, result);
		// setCellData();
	}

	private void addDataAfterTest(IInvokedMethod method, ITestResult result) {
		// TODO Auto-generated method stub
		final ArrayList<String> dataAfterTest = new ArrayList<String>();
		final String timeStartRun = getTime(result.getStartMillis());
		final String timeToRun = timeStartRun + " Take time: "
				+ String.valueOf(TimeUnit.MILLISECONDS.toSeconds(result.getEndMillis() - result.getStartMillis()))
				+ "s";
		try {
			if (result.getStatus() == ITestResult.SUCCESS) {
				dataAfterTest.add("PASSED");
				dataAfterTest.add(result.getTestContext().getName());
				dataAfterTest.add("");
				dataAfterTest.add(timeToRun);
				allDataAfterTest.put(method.getTestMethod().getMethodName(), dataAfterTest);

			} else if (result.getStatus() == ITestResult.FAILURE) {
				dataAfterTest.add("FAILED");
				dataAfterTest.add(result.getTestContext().getName());
				dataAfterTest.add(result.getThrowable().getMessage());
				dataAfterTest.add(timeToRun);
				allDataAfterTest.put(method.getTestMethod().getMethodName(), dataAfterTest);

			} else if (result.getStatus() == ITestResult.SUCCESS_PERCENTAGE_FAILURE) {
				dataAfterTest.add("FAILED");
				dataAfterTest.add(result.getTestContext().getName());
				dataAfterTest.add(result.getThrowable().getMessage());
				dataAfterTest.add(timeToRun);
				allDataAfterTest.put(method.getTestMethod().getMethodName(), dataAfterTest);
			}
		} catch (final Exception e) {
			log.info("\nLog Message::@AfterMethod: Exception caught");
			e.printStackTrace();
		}

	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult result) {
		// TODO Auto-generated method stub
	}

	private void setCellData(ITestContext context) {
		try {
			final FileInputStream file = new FileInputStream(new File(Constant.reportFilePath));
			excelBook = new HSSFWorkbook(file);
			excelSheet = excelBook.getSheet("Test");
			int lastRowNum = excelSheet.getLastRowNum();
			boolean dup = false;
			// Read row Excel
			for (int i = 1; i <= lastRowNum; i++) {
				// Set cell
				testCaseName = excelSheet.getRow(i).getCell(0);
				platformCellValue = excelSheet.getRow(i).getCell(1);
				resultCellValue = excelSheet.getRow(i).getCell(2);
				exceptionCell = excelSheet.getRow(i).getCell(3);
				timeToRunCell = excelSheet.getRow(i).getCell(4);

				// Read dataAfterTest
				for (final Map.Entry<String, ArrayList<String>> entry : allDataAfterTest.entrySet()) {
					if (entry.getKey().equals(testCaseName.getStringCellValue())) {

						// Check platform
						if (platformCellValue.getStringCellValue().isEmpty()) {

							// Set excel values
							resultCellValue.setCellValue(entry.getValue().get(0));
							platformCellValue.setCellValue(entry.getValue().get(1));
							exceptionCell.setCellValue(entry.getValue().get(2));
							timeToRunCell.setCellValue(entry.getValue().get(3));
						} else {
							while (i < lastRowNum
									&& excelSheet.getRow(i).getCell(0).toString()
											.equals(excelSheet.getRow(i + 1).getCell(0).toString())
									&& !excelSheet.getRow(i + 1).getCell(1).getStringCellValue().isEmpty()) {
								i++;
							}

							// Add more row
							excelSheet.shiftRows(i + 1, lastRowNum, 1);
							final Row newRow = excelSheet.createRow(i + 1);

							// Copy testcase to new Cell
							final Cell newTestCaseCell = newRow.createCell(0);
							newTestCaseCell.setCellValue(testCaseName.getStringCellValue());

							// Add new Cell
							for (int j = 1; j <= 4; j++) {
								newRow.createCell(j);
							}

							// Update last row number
							lastRowNum = excelSheet.getLastRowNum();
							dup = true;
							break;
						}

					}
				}
			}
			if (dup == true) {
				mergeCell(excelSheet);
			}
			final FileOutputStream fileOut = new FileOutputStream(new File(Constant.reportFilePath));
			excelBook.write(fileOut);
			fileOut.flush();
			fileOut.close();

		} catch (final Exception e) {
			System.out.print(e.getMessage());
		}
	}

	private static void mergeCell(HSSFSheet excelSheet) {
		// Unmerge cell before
		for (int i = excelSheet.getNumMergedRegions() - 1; i >= 0; i--) {
			excelSheet.removeMergedRegion(i);
		}
		int first = 1;
		int last = 1;
		int i = 1;
		while (i < excelSheet.getPhysicalNumberOfRows()) {
			first = i;
			last = i;
			for (int j = i + 1; j < excelSheet.getPhysicalNumberOfRows(); j++) {
				final Cell cell = excelSheet.getRow(i).getCell(0);
				final Cell newcell = excelSheet.getRow(j).getCell(0);
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (cell.getRichStringCellValue().getString().trim().equals(newcell.toString())) {
						last = j;
					}
				}
			}
			if (first != last) {
				excelSheet.addMergedRegion(new CellRangeAddress(first, last, 0, 0));
			}
			i = last + 1;
		}
	}

	private String getTime(long millis) {
		final Calendar calendar = Calendar.getInstance();
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		calendar.setTimeInMillis(millis);
		return format.format(calendar.getTime());
	}

}
