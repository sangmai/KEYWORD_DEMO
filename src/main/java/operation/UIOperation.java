package operation;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import common.AbstractPage;

public class UIOperation extends AbstractPage {

	WebDriver driver;
	Map<String, String> map = new HashMap<String, String>();

	public UIOperation(WebDriver driver) {
		this.driver = driver;
	}

	public void perform(Properties p, String operation, String objectName, String objectType, String value,
			String variable) throws Exception {
		System.out.println("");
		switch (operation.toUpperCase()) {
		case "CLICK":
			// Perform click
			click(driver, this.getXpath(p, objectName, objectType));
			break;
		case "SETTEXT":
			// Set text on control
			if (variable.equals("")) {
				type(driver, this.getXpath(p, objectName, objectType), value);
			}else{
				for (Map.Entry<String, String> entry : map.entrySet()) {
					if (entry.getKey().equals(variable)) {
						System.out.println(entry.getValue());
						type(driver, this.getXpath(p, objectName, objectType), entry.getValue());
					}
				}
			}			
			break;

		case "GOTOURL":
			// Get url of application
			driver.get(p.getProperty(value));
			break;
		case "GETTEXT":
			// Get text of an element
			getText(driver, this.getXpath(p, objectName, objectType));
			break;
		case "SETVARIABLE":
			map.put(objectName, getText(driver, this.getXpath(p, objectName, objectType)));
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

	private String getXpath(Properties p, String objectName, String objectType) throws Exception {
		// Find by xpath --> Return Xpath as String
		if (objectType.equalsIgnoreCase("XPATH")) {

			return p.getProperty(objectName).toString();
		}
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
		else {
			throw new Exception("Wrong object type");
		}
	}
}
