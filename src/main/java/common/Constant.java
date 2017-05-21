package common;

import org.openqa.selenium.WebDriver;

public class Constant {
	public static WebDriver driver = null;
	public static String currentWindows = null;
	public static final int longWaitTime = 8;
	public static final int shortWaitTime = 5;
	public static final String email = "Automation";
	public static final String Homepage_url = "http://demo.guru99.com/V4/";
	public static final String Live_Homepage_url = "http://live.guru99.com";
	public static final String configFilePath = "src/configurations/configuration.xml";
	public static final String dataFilePath = "src/test/data.xml";
	public static final String reportFilePath = "Report.xls";
	public static final String FOLDER_DOWNLOAD_ONE_WIN = "src/configurations/download";
	// ===================driver===============================================
	public static final String driverPath = "src//test//resources//";
	public static final String ieWebDriver = "webdriver.ie.driver";
	public static final String ieServerDriver = "IEDriverServer.exe";
	public static final String chromeWebDriver = "webdriver.chrome.driver";
	public static final String chromeServerDriver = "chromedriver.exe";
	public static final String geckoServerDriver = "geckodriver.exe";
	public static final String geckoWebDriver = "webdriver.gecko.driver";
}
