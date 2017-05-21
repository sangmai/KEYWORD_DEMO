package common;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AutomationControl {
	public WebElement findElement(WebDriver driver, String locator) {
		WebElement element = null;
		element = driver.findElement(By.xpath(locator));
		return element;
	}

	public WebElement findElement(WebDriver driver, String specialLocator, String value) {
		WebElement element = null;
		String locator = String.format(specialLocator, value);
		element = driver.findElement(By.xpath(locator));
		return element;
	}

	public WebElement findElement(WebDriver driver, String specialLocator, String value1, float value2) {
		WebElement element = null;
		String locator = String.format(specialLocator, value1, value2);
		element = driver.findElement(By.xpath(locator));
		return element;
	}

	public WebElement findElement(WebDriver driver, String specialLocator, String value1, String value2) {
		WebElement element = null;
		String locator = String.format(specialLocator, value1, value2);
		element = driver.findElement(By.xpath(locator));
		return element;
	}

	public WebElement findElement(WebDriver driver, String specialLocator, String value1, String value2,
			String value3) {
		WebElement element = null;
		String locator = String.format(specialLocator, value1, value2, value3);
		element = driver.findElement(By.xpath(locator));
		return element;
	}

	public By getBy(WebDriver driver, String locator) {
		By by = null;
		by = By.xpath(locator);
		return by;
	}

	public By getBy(WebDriver driver, String specialControl, String value) {
		By by = null;
		String control = String.format(specialControl, value);
		by = By.xpath(control);
		return by;
	}

	public List<WebElement> findElements(WebDriver driver, String controlName) {
		List<WebElement> lstElement = null;
		lstElement = driver.findElements(By.xpath(controlName));
		return lstElement;
	}

	public List<WebElement> findElements(WebDriver driver, String controlName, String value) {
		List<WebElement> lstElement = null;
		String control = String.format(controlName, value);
		lstElement = driver.findElements(By.xpath(control));
		return lstElement;
	}

	public List<WebElement> findElements(WebDriver driver, String controlName, String value1, String value2) {
		List<WebElement> lstElement = null;
		String control = String.format(controlName, value1, value2);
		lstElement = driver.findElements(By.xpath(control));
		return lstElement;
	}

	public void setControlValue(String controlValue) {
		this.controlValue = controlValue;
	}

	public String getControlValue() {
		return controlValue;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public String getControlType() {
		return controlType;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	// private static AutomationControl control = null;
	private String page;
	private String controlValue;
	private String controlType;
	protected final Log log = LogFactory.getLog(AutomationControl.class);

}
