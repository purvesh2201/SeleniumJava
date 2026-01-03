package com.Automation.ActionDriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.Automation.Base.BaseClass;
import com.Automation.Utilities.ExtentManager;
import com.Automation.Utilities.LoggerManager;

public class ActionDriver {
	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger log = BaseClass.log;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		
		int explicitwait = Integer.parseInt(BaseClass.getProp().getProperty("explicitwait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitwait));
	}

	// method to click element
	public void click(By by) {
		String elementDescrtiption = getElementDescription(by);
		try {
			applyBorder(by, "green");
			waitforElementtobeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked an element : "+elementDescrtiption);
			log.info("Clicked an element : "+elementDescrtiption);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(BaseClass.getDriver(), "unable to "
					+ "click an element : "+elementDescrtiption, elementDescrtiption+" un"
							+ "able to click");
			log.error("Unable to click Element " + e.getMessage());
		}
	}

	// method to enter text into input field
	public void enterText(By by, String Value) {
		try {
			applyBorder(by, "green");
			waitforElementtobeVisible(by);
			driver.findElement(by).clear();
			driver.findElement(by).sendKeys(Value);
			log.info("Element Description is -> "+getElementDescription(by));
			log.info("User has entered value : "+Value);
		} catch (Exception e) {
			applyBorder(by, "red");
			log.error("Unable to Enter Text " + e.getMessage());
		}
	}

	// method to get text from input field
	public String getText(By by) {
		try {
			applyBorder(by, "green");
			waitforElementtobeVisible(by);
			log.info("Element Description is -> "+getElementDescription(by));
			String value = driver.findElement(by).getText();
			return value;
		} catch (Exception e) {
			applyBorder(by, "red");
			log.error("Unable to get Text from input field " + e.getMessage());
			return "Null";
		}
	}

	// compare two text
	public boolean compareText(By by, String expectedText) {
		try {
			waitforElementtobeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), actualText+" <- this is actual text", expectedText+" <- this is expected text");
				log.info("Text are matching : " + actualText + " equals " + expectedText);
				return true;
			} else {
				applyBorder(by, "red");
				ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparison Failed!", "Text Comparison Failed! "+actualText+ " not equals "+expectedText);
				log.error("Text are not matching : " + actualText + " does not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			log.error("Unable to compare two Text " + e.getMessage());
			return false;
		}
	}

	// method to check if element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitforElementtobeVisible(by);
			applyBorder(by, "green");
			ExtentManager.logStep("Element Description is -> "+getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element Description is -> "+getElementDescription(by), "Element Description is -> "+getElementDescription(by));
			log.info("Element Description is -> "+getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			log.error("Element is not Displayed "+e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not dispalyed ", " Element is not dispalyed "+getElementDescription(by));
			return false;
		}
	}
	
	//wait for web page to load
	public void waitForPageLoad(int timeoutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeoutInSec))
			.until(WebDriver -> ((JavascriptExecutor)WebDriver)
			.executeScript("return document.readyState").equals("complete"));
			log.info("Page Load Succesfully");
		} catch (Exception e) {
			log.info("Page did not load within "+timeoutInSec+" "+e.getMessage());
		}
	}
	

	//scroll to an element 
	public void scrolltoElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0],scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by, "red");
			log.info("Unable to Locate Element "+e.getMessage());
		}
	}
	
	// wait for element to be clickable
	private void waitforElementtobeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			log.info("Element is not clickable " + e.getMessage());
		}
	}

	// wait for element to ve visible
	private void waitforElementtobeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			log.info("Element is not Visible " + e.getMessage());
		}
	}
	
	//Method to get description of element using by locator 
	public String getElementDescription(By locator) {
		
		if(driver==null) return "Driver is NULL";
		if(locator==null) return "Locator is NULL";
		
		try {
			WebElement element = driver.findElement(locator);
			String Name = element.getDomAttribute("name");
			String ID = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeholder");
			
			// Return Description Based on element attribute
			if(isNotEmpty(Name)) {return "Element with Name :"+Name;}
			else if(isNotEmpty(ID)) {return "Element with ID :"+ID;}
			else if(isNotEmpty(text)) {return "Element with text :"+truncate(text, 50);}
			else if(isNotEmpty(className)) {return "Element with class :"+className;}
			else if(isNotEmpty(placeHolder)) {return "Element with placeHolder :"+placeHolder;}
		} catch (Exception e) {
			log.error("Unable to Describe the element "+e.getMessage());
		}
		return "Unable to describe the element";
		
	}
	
	//utility method to check string is not null or empty 
	private Boolean isNotEmpty(String value) {
		return value!=null && !value.isEmpty();
	}
	
	//utility method to truncate long string 
	private String truncate(String value, int maxlength) {
		if(value==null || value.length()<=maxlength) {
			return value;
		}
		else {
			return value.substring(0, maxlength);
		}
				
	}
	
	//utility method to border a element 
	public void applyBorder(By by,String color) {
		try {
			//Locate the element
			WebElement element = driver.findElement(by);
			//Apply the border
			String script = "arguments[0].style.border='3px solid "+color+"'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			log.info("Applied the border with color "+color+ " to element: "+getElementDescription(by));
		} catch (Exception e) {
			log.warn("Failed to apply the border to an element: "+getElementDescription(by),e);
		}
	}
}
