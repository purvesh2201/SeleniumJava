package com.Automation.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.Automation.ActionDriver.ActionDriver;
import com.Automation.Base.BaseClass;

public class HomePage {
	
	private ActionDriver actiondriver;
	
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIdButton = By.className("oxd-userdropdown-name");
	private By logout = By.xpath("//a[text()='Logout']");
	private By OrangeHRMlogo = By.xpath("//div[@class='oxd-brand-banner']//img");
	
	//initilaize the constructor 
/*	public HomePage(WebDriver driver) {
		this.actiondriver = new ActionDriver(driver);
	}
*/	
	public HomePage(WebDriver driver) {
		this.actiondriver = BaseClass.getActionDriver();
	}
	
	//admin tab is visible or not  
	public Boolean isAdminTabVisible() {
		return actiondriver.isDisplayed(adminTab);

	}
	
	public Boolean VerifyHRMLogo() {
		return actiondriver.isDisplayed(OrangeHRMlogo);
	}
	
	// method to perform logout operation 
	public void logout() {
		actiondriver.click(userIdButton);
		actiondriver.click(logout);
		actiondriver.waitForPageLoad(5);
	}

}
