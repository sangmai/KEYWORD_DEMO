package operation;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import common.AbstractPage;

public class UIOperation extends AbstractPage{

	WebDriver driver;

	public UIOperation(WebDriver driver) {
		this.driver = driver;
	}

	public void perform(Properties p, String operation, String objectName, String objectType, String value)
			throws Exception {
		System.out.println("");
		switch (operation.toUpperCase()) {
		case "CLICK":
			// Perform click
			click(driver, this.getObject(p, objectName, objectType));
			break;
		case "SETTEXT":
			// Set text on control
			type(driver, this.getObject(p, objectName, objectType), value);
			break;

		case "GOTOURL":
			// Get url of application
			driver.get(p.getProperty(value));
			break;
		case "GETTEXT":
			// Get text of an element
			getText(driver, this.getObject(p, objectName, objectType));
			break;

		default:
			break;
		}
	}

	/**
	 * Find element BY using object type and value
	 * 
	 * @param p
	 * @param objectName
	 * @param objectType
	 * @return
	 * @throws Exception
	 */
	private String getObject(Properties p, String objectName, String objectType) throws Exception {
		// Find by xpath
		if (objectType.equalsIgnoreCase("XPATH")) {

			return p.getProperty(objectName).toString();
		}
//		// find by class
//		else if (objectType.equalsIgnoreCase("CLASSNAME")) {
//
//			return By.className(p.getProperty(objectName));
//
//		}
//		// find by name
//		else if (objectType.equalsIgnoreCase("NAME")) {
//
//			return By.name(p.getProperty(objectName));
//
//		}
//		// Find by css
//		else if (objectType.equalsIgnoreCase("CSS")) {
//
//			return By.cssSelector(p.getProperty(objectName));
//
//		}
//		// find by link
//		else if (objectType.equalsIgnoreCase("LINK")) {
//
//			return By.linkText(p.getProperty(objectName));
//
//		}
//		// find by partial link
//		else if (objectType.equalsIgnoreCase("PARTIALLINK")) {
//
//			return By.partialLinkText(p.getProperty(objectName));

//		} else {
		else{
			throw new Exception("Wrong object type");
		}
	}
}
