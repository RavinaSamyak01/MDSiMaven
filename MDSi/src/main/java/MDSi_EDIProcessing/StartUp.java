package MDSi_EDIProcessing;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import io.github.bonigarcia.wdm.WebDriverManager;

public class StartUp {
	static WebDriver driver;
	public static Properties storage = new Properties();

	@BeforeSuite
	public void startUp() throws IOException {
		storage = new Properties();
		FileInputStream fi = new FileInputStream(".\\src\\main\\resources\\config.properties");
		storage.load(fi);
		// --Opening Chrome Browser
		DesiredCapabilities capabilities = new DesiredCapabilities();
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless", "--window-size=1920,1200");
		options.addArguments("--incognito");
		options.addArguments("--test-type");
		options.addArguments("--no-proxy-server");
		options.addArguments("--proxy-bypass-list=*");
		options.addArguments("--disable-extensions");
		options.addArguments("--no-sandbox");
		options.addArguments("enable-automation");
		options.addArguments("--dns-prefetch-disable");
		options.addArguments("--disable-gpu");
		String downloadFilepath = System.getProperty("user.dir") + "\\src\\main\\resources\\Downloads";
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.prompt_for_download", "false");
		chromePrefs.put("safebrowsing.enabled", "false");
		chromePrefs.put("download.default_directory", downloadFilepath);
		options.setExperimentalOption("prefs", chromePrefs);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		capabilities.setPlatform(Platform.ANY);
		driver = new ChromeDriver(options);

	}

	@AfterSuite
	public void Complete() throws Exception {
		driver.close();
	}
}
