package com.Automation.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.Automation.ActionDriver.ActionDriver;
import com.Automation.Base.BaseClass;

public class LoginPage {
	
	private ActionDriver actiondriver;
	
	private By username = By.name("username");
	private By password = By.name("password");
	private By login = By.xpath("//button[text()=' Login ']");
	private By errormsg = By.xpath("//p[text()='Invalid credentials']");
	
/*	public LoginPage(WebDriver driver) {
		this.actiondriver = new ActionDriver(driver);
	} 
*/
	public LoginPage(WebDriver driver) {
		this.actiondriver = BaseClass.getActionDriver();
	}
	
	//method to perform login 
	public void login(String uname,String passwd) {
		actiondriver.enterText(username, uname);
		actiondriver.enterText(password, passwd);
		actiondriver.click(login);
		actiondriver.waitForPageLoad(5);
	}
	
	//method to check for error message 
	public Boolean isErrorDispalyed(String errortxt) {
		System.out.println(actiondriver.compareText(errormsg,errortxt)+"!!!!!!!!");
		actiondriver.waitForPageLoad(5);
		return actiondriver.compareText(errormsg,errortxt);
		
	}

}
