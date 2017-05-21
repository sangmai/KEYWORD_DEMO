package common;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class AbstractTest {
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

}
