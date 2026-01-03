package com.Automation.Base;

import java.io.FileInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.Automation.ActionDriver.ActionDriver;
import com.Automation.Utilities.ExtentManager;
import com.Automation.Utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
//	protected static WebDriver driver;
//	private static ActionDriver actiondriver;
	public static final Logger log = LoggerManager.getLogger(BaseClass.class);
	
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>(); 
	private static ThreadLocal<ActionDriver> actiondriver = new ThreadLocal<>(); 
	
	@BeforeSuite
	public void loadConfig() throws IOException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		log.info("config.properties file loaded");
		//ExtentManager.getReporter(); -> this has been implemented in Test Listener
		}

	private void launchBrowser() {
		String browser = prop.getProperty("browser");
		if (browser.equals("chrome")) {
			//driver = new ChromeDriver();
			ChromeOptions options = new ChromeOptions();
		//	options.addArguments("--headless"); // Run Chrome in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU for headless mode
			options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--headless=new");
			options.addArguments("--screen-info={0,0 1920x1080}");
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments
			driver.set(new ChromeDriver(options));
			ExtentManager.registerDriver(getDriver());
			log.info("Chrome Driver Instance is Created");
		} else if (browser.equalsIgnoreCase("firefox")) {
			//driver = new FirefoxDriver();
			FirefoxOptions options = new FirefoxOptions();
		//	options.addArguments("--headless"); // Run Firefox in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
			options.addArguments("--width=1920"); // Set browser width
			options.addArguments("--height=1080"); // Set browser height
			options.addArguments("--headless=new");
			options.addArguments("--screen-info={0,0 1920x1080}");
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD environments
			options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments
			driver.set(new FirefoxDriver(options));
			ExtentManager.registerDriver(getDriver());
			log.info("FireFox Driver Instance is Created");
		} else if (browser.equalsIgnoreCase("edge")) {
			//driver = new EdgeDriver();
			EdgeOptions options = new EdgeOptions();
		//	options.addArguments("--headless"); // Run Edge in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU acceleration
			options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--headless=new");
			options.addArguments("--screen-info={0,0 1920x1080}");
			options.addArguments("--disable-notifications"); // Disable pop-up notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD
			options.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes
			driver.set(new EdgeDriver(options));
			ExtentManager.registerDriver(getDriver());
			log.info("Edge Driver Instance is Created");
		} else {
			throw new IllegalArgumentException("Browser Not Supported " + browser);
		}

	}

	private void configureBrowser() {
		// implicit wait
		int implicitwait = Integer.parseInt(prop.getProperty("implicitwait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitwait));

		// maximize the driver
		getDriver().manage().window().maximize();

		// navigate to URL
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("Failed to Navigate to URL " + e.getMessage());
		}
	}

	@BeforeMethod
	public synchronized void setup() throws IOException {
		log.info("Setting up webdriver for " + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		log.info("Browser Launched and Maximised");
		//staticWait(2);
		// Initialize action driver only once 
	/*	if(actiondriver == null) {
			actiondriver = new ActionDriver(driver);
			log.info("ActionDriver Instance is created only once "
					+ "- Singleton Design Pattern");
			log.info("Thread ID is "+Thread.currentThread().getId());
		}
	*/	
		actiondriver.set(new ActionDriver(getDriver()));
		log.info("ACtion Driver Initialized - "+Thread.currentThread().getId());
		
	}

	@AfterMethod
	public synchronized void teardown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.info("Failed to close browser " + e.getMessage());
			}
		} 
		
		//driver = null;
		//actiondriver=null;
		log.info("WebDriver Instance is closed"
				+ " - Singleton design pattern");
		driver.remove();
		actiondriver.remove();
		// ExtentManager.endTest();
	}

/*
	// driver getter method
	public WebDriver getDriver() {
		return driver;
	}
*/
	
	// driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}
	
	
	// getter method for Action driver 
	public static ActionDriver getActionDriver() {
		if (actiondriver.get()==null) {
			log.info("ActionDriver is not initialized ");
			throw new IllegalStateException("ActionDriver is not initialized");
		} return actiondriver.get();
	}
	
	// getter method for action driver
	public static WebDriver getDriver() {
		if (driver.get()==null) {
			log.info("WebDriver is not initialized ");
			throw new IllegalStateException("WebDriver is not initialized");
		} return driver.get();
	}
	
	// getter method for prop 
	public static Properties getProp() {
		return prop;
	}
	
	public void staticWait(int waitimeinSeconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(waitimeinSeconds));
	}

}
