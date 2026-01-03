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

public class HomePageTest extends BaseClass{
	
	private LoginPage loginpage;
	private HomePage homepage;
	
	@BeforeMethod
	public void setuppages() {
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}
	
	@Test(dataProvider = "validLoginData",dataProviderClass = DataProviders.class)
	public void verifyOrangeHRMlogo(String username,String password) {
		ExtentManager.logStep("Entering username and password");
		loginpage.login(username,password);
		ExtentManager.logStep("Logged in Succesfully");
		Assert.assertTrue(homepage.VerifyHRMLogo(),"HRM Logo is not visible");
		ExtentManager.logStep("OrangeHRm Logo is validated succesfully");
	}

}
