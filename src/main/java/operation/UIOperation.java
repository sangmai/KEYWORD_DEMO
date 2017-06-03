package operation;

import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

import common.AbstractAction;
import common.AbstractTest;
import common.Constant;

public class UIOperation extends AbstractAction {

	WebDriver driver;

	public UIOperation(WebDriver driver) {
		this.driver = driver;
	}

	public void perform(Properties p, String actions, String objectName, String value, String variable)
			throws Exception {
		switch (actions.toUpperCase()) {
		case "CLICK":
			// Perform click
			waitForElement(driver, this.getXpath(p, objectName), Constant.longWaitTime);
			click(driver, this.getXpath(p, objectName));
			break;
		case "SETTEXT":
			// Type value
			if (variable.equals("")) {
				waitForElement(driver, this.getXpath(p, objectName), Constant.longWaitTime);
				type(driver, this.getXpath(p, objectName), getValue(value));
			} else {
				// Type variable
				for (Map.Entry<String, String> entry : Constant.mapVarialbe.entrySet()) {
					if (entry.getKey().equals(variable)) {
						waitForElement(driver, this.getXpath(p, objectName), Constant.longWaitTime);
						type(driver, this.getXpath(p, objectName), entry.getValue());
					}
				}
			}
			break;

		case "GOTOURL":
			// Get url of application
			driver.get(p.getProperty(getValue(value)));
			break;
		case "GETTEXT":
			// Get text of an element
			getText(driver, this.getXpath(p, objectName));
			break;
		case "SETVARIABLE":
			Constant.mapVarialbe.put(objectName, getText(driver, this.getXpath(p, objectName)));
			break;
		case "WAIT":
			sleep(2);
			break;
		case "ACCEPTJAVASCRIPT":
			acceptJavascriptAlert(driver);
			break;
		case "VERIFYTRUE":
			waitForElement(driver, this.getXpath(p, objectName), value, Constant.longWaitTime);
			verifyTrue(isControlDisplayed(driver, this.getXpath(p, objectName), value));
			break;
		case "RUNTESTCASE":

			break;
		default:
			break;
		}
	}

	public boolean processCondition(WebDriver driver, String condition) {
		switch (condition) {
		case "isAlertPresent":
			return AbstractAction.isAlertPresent(driver);
		}
		return false;
	}

	public String getValue(String value) {
		switch (value) {
		case "RANDOMEMAIL":
			return AbstractTest.RandomEmail();
		}

		return value;

	}

	private String getXpath(Properties p, String objectName) {
		// Find by xpath --> Return Xpath as String
		String xpath = null;
		try {
			xpath = p.getProperty(objectName).toString();
			// }
			// // find by class
			// else if (objectType.equalsIgnoreCase("CLASSNAME")) {
			//
			// return By.className(p.getProperty(objectName));
			//
			// }
			// // find by name
			// else if (objectType.equalsIgnoreCase("NAME")) {
			//
			// return By.name(p.getProperty(objectName));
			//
			// }
			// // Find by css
			// else if (objectType.equalsIgnoreCase("CSS")) {
			//
			// return By.cssSelector(p.getProperty(objectName));
			//
			// }
			// // find by link
			// else if (objectType.equalsIgnoreCase("LINK")) {
			//
			// return By.linkText(p.getProperty(objectName));
			//
			// }
			// // find by partial link
			// else if (objectType.equalsIgnoreCase("PARTIALLINK")) {
			//
			// return By.partialLinkText(p.getProperty(objectName));

			// } else {
		} catch (Exception e) {
			// throw new Exception("Wrong object type");
			e.printStackTrace();
		}
		return xpath;

	}
}
