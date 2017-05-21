package common;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.browserstack.local.Local;
import com.google.gson.JsonObject;

public class BrowserManager {
	/**
	 * @author Sang Mike
	 */
	private WebDriver driver;
	private static final Log log = LogFactory.getLog(BrowserManager.class);
	private Local l;

	public WebDriver openBrowser(String browser) {

		try {
			if (browser.equals("IE")) {
				log.info("Executing on IE");
				DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
				ieCapabilities.setCapability("nativeEvents", false);
				ieCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
				ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
				ieCapabilities.setCapability("disable-popup-blocking", true);
				ieCapabilities.setCapability("enablePersistentHover", true);
				System.setProperty(Constant.ieWebDriver, Constant.driverPath + Constant.ieServerDriver);
				driver = new InternetExplorerDriver(ieCapabilities);
			} else if (browser.equals("Chrome")) {
				log.info("Executing on CHROME");
				System.setProperty(Constant.chromeWebDriver, Constant.driverPath + Constant.chromeServerDriver);
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--disable-extensions");
				driver = new ChromeDriver(options);
			} else if (browser.equals("FF")) {
				log.info("Executing on FF, Run with gecko");
				System.out.println("FF is running");
				// System.setProperty(Constant.geckoWebDriver,
				// Constant.driverPath + Constant.geckoServerDriver);
				String proxy = "donkey.cybersoft.vn";
				int port = 8080;
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("proxyType", "MANUAL");
				jsonObject.addProperty("httpProxy", proxy);
				jsonObject.addProperty("httpProxyPort", port);
				jsonObject.addProperty("sslProxy", proxy);
				jsonObject.addProperty("sslProxyPort", port);
				DesiredCapabilities cap = new DesiredCapabilities();
				cap.setCapability("proxy", jsonObject);
				GeckoDriverService service = new GeckoDriverService.Builder()
						.usingDriverExecutable(new File(Constant.driverPath + Constant.geckoServerDriver))
						.usingAnyFreePort().usingAnyFreePort().build();
				service.start();
				driver = new FirefoxDriver(cap, cap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}

	public WebDriver remoteBrowser(String config_file, String environment, String url) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/" + config_file));
		JSONObject envs = (JSONObject) config.get("environments");

		DesiredCapabilities capabilities = new DesiredCapabilities();

		Map<String, String> envCapabilities = (Map<String, String>) envs.get(environment);
		Iterator it = envCapabilities.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
		}

		Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
		it = commonCapabilities.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (capabilities.getCapability(pair.getKey().toString()) == null) {
				capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
			}
		}

		String username = System.getenv("BROWSERSTACK_USERNAME");
		if (username == null) {
			username = (String) config.get("user");
		}

		String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
		if (accessKey == null) {
			accessKey = (String) config.get("key");
		}

		if (capabilities.getCapability("browserstack.local") != null
				&& capabilities.getCapability("browserstack.local") == "true") {
			l = new Local();
			Map<String, String> options = new HashMap<String, String>();
			options.put("key", accessKey);
			l.start(options);
		}
		driver = new RemoteWebDriver(
				new URL("https://sangxuanmai1:oqwZ4qS9ZDcTymL2DzUD@hub-cloud.browserstack.com/wd/hub"), capabilities);
		driver.get(url);
		return driver;
	}

	public WebDriver getDriver() {
		return driver;
	}
}
