package com.Automation.Test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.Automation.Base.BaseClass;
import com.Automation.Pages.HomePage;
import com.Automation.Pages.LoginPage;
import com.Automation.Utilities.DataProviders;
import com.Automation.Utilities.ExtentManager;

public class LoginPageTest extends BaseClass {
	
	private LoginPage loginpage;
	private HomePage homepage;
	
	@BeforeMethod
	public void setupPages() {
		
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
		
	}
	
	@Test(dataProvider = "validLoginData",dataProviderClass = DataProviders.class)
	public void verifyLoginTest(String username,String password) {
		ExtentManager.logStep("Entering username and password");
		loginpage.login(username,password);
		Assert.assertTrue(homepage.isAdminTabVisible(),"Admin Tab should be visible");
		ExtentManager.logStep("Logged in Succesfully");
		homepage.logout();
		ExtentManager.logStep("Log out Succesful");
		
	}
	
	@Test(dataProvider = "invalidLoginData",dataProviderClass = DataProviders.class)
	public void invalidlogintext(String username,String password) {
		loginpage.login(username,password);
		System.out.println(loginpage.isErrorDispalyed("Invalid credentials"));
		Assert.assertTrue(loginpage.isErrorDispalyed("Invalid credentials1"));
		ExtentManager.logStep("Either username or password is incorrect");
	}
	

}
