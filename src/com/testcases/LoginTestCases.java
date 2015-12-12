package com.testcases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class LoginTestCases {

	// try making this object outside that too static
	WebDriver wd = null; // this is problem if we want to take this out side of
							// class for screen shots

	// For login page global variables declared as they are going to get used
	// every where.
	// we are going to perform 3 tests on user name and same button
	// just to reduce redundancy

	WebElement webEleUserName = null;
	WebElement webEleSubButton = null;
	excelReader exlreader = new excelReader();
	public InputStream fis = null;
	excelread xpar = new excelread();

	@BeforeClass
	public void beforeClass() {

		// wd = new FirefoxDriver();//Don't use this
		// instead we will get from another class

		WebDriverInstance.startDriver();
		wd = WebDriverInstance.getDriverInstance();

		wd.get("file:///D:/offline%20website/login.html");
		webEleUserName = wd.findElement(By.name("fname"));
		webEleSubButton = wd.findElement(By.xpath("/html/body/form/input[2]"));
	}

	/*
	 * Always run is true If not given it starts throwing exception if any test
	 * case fails.
	 */

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		webEleUserName.clear();
	}

	@Test(priority = 0, alwaysRun = true, dataProvider = "chkblankuname")
	public void checkBlankUserName(String pass, String message, String blankmessage, String Cmessage) {
		// blank
		webEleUserName.sendKeys(pass);
		webEleSubButton.submit();

		Alert alert = wd.switchTo().alert();
		String alertMsg = alert.getText();

		// assert must be at last if it is failed then alert should get accepted
		// else alert box will remain open

		alert.accept();
		Assert.assertEquals(alertMsg, message);
		Assert.assertNotEquals(alertMsg, blankmessage);
		Assert.assertNotEquals(alertMsg, Cmessage);

		/*
		 * Assert.assertEquals(alertMsg, "Name11 must be filled out");
		 * Assert.assertNotEquals(alertMsg, " ");
		 * Assert.assertNotEquals(alertMsg, "password is javabykiran");
		 */
	}

	@DataProvider
	public Object[][] chkblankuname() throws FileNotFoundException {
		FileInputStream fis = new FileInputStream("offlineweb_data.xls");
		String sheetName = "blank";
		try {
			xpar.loadFromSpreadsheet(fis, sheetName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xpar.getData();
		// Object[][] arrayObject =xpar.XcelParserTestNGLogin(fis,"blank");
		// return arrayObject;
		// new Object[] { "jbk", "password is javabykiran" , " ","Name must be
		// filled out"},

	};

	@Test(priority = 1, alwaysRun = true, dataProvider = "logintestdata")
	public void checkInvalidUserName(String pass, String message, String blankmessage, String Cmessage) {
		// invalid data
		// webEleUserName.sendKeys("jbk");
		webEleUserName.sendKeys(pass);

		webEleSubButton.submit();

		Alert alert = wd.switchTo().alert();
		String alertMsg = alert.getText();

		alert.accept();

		// assert must be at last if it is failed then alert should get accepted
		// else alert box will remain open

		/*
		 * Assert.assertEquals(alertMsg, "password is javabykiran");
		 * Assert.assertNotEquals(alertMsg, " ");
		 * Assert.assertNotEquals(alertMsg, "Name must be filled out");
		 */
		Assert.assertEquals(alertMsg, message);
		Assert.assertNotEquals(alertMsg, blankmessage);
		Assert.assertNotEquals(alertMsg, Cmessage);
	}

	@DataProvider
	public Object[][] logintestdata() throws FileNotFoundException {
		//Object[][] arrayObject = exlreader.getExcelData("D:/offlineweb_data.xls", "Login");
		//return arrayObject;
		FileInputStream fis = new FileInputStream("offlineweb_data.xls");
		String sheetName = "Login";
		try {
			xpar.loadFromSpreadsheet(fis, sheetName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xpar.getData();
		// new Object[] { "jbk", "password is javabykiran" , " ","Name must be
		// filled out"},

	};

	@Test(priority = 2, alwaysRun = true)
	public void checkCorrectUserName() {
		// invalid data
		webEleUserName.sendKeys("javabykiran");
		webEleSubButton.submit();
		// if header appears means user name is correct
		WebElement webEleHeader = wd.findElement(By.xpath("/html/body/blockquote/table/tbody/tr[1]/td/h1/em/strong"));

		Assert.assertEquals(webEleHeader.getText().trim(), "Employee Report");

		WebElement webEleUserLabel = wd.findElement(By.xpath("html/body/blockquote/table/tbody/tr[2]/td[2]/strong/em"));

		Assert.assertTrue(webEleUserLabel.getText().trim().contains("User Name"));

	}

	@AfterMethod
	public void afterMethod() {

	}

	@AfterClass
	public void afterClass() {
		wd.quit();
	}

}
