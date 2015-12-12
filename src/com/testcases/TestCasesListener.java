package com.testcases;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

//if we extend by TestListenerAdapter no need to implement  ITestListener
// iternally this class implements it

public class TestCasesListener extends TestListenerAdapter  implements ITestListener{

	
	WebDriver driver=null;
	
	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println("onTestSuccess");
		
	}

	/* (non-Javadoc)
	 * @see org.testng.ITestListener#onTestFailure(org.testng.ITestResult)
	 * 
	 * Here we will be taking screen shot o failer incidenet.
	 * we will be saving in folder called logintestcase 
	 * so that it will be easy to trace.
	 */
	@Override
	public void onTestFailure(ITestResult result) {
	
		String filePath = "D:\\SCREENSHOTS\\LOGIN";
		
		//Reporter.log(s)
		 File scrFile = ((TakesScreenshot) WebDriverInstance.getDriverInstance()).getScreenshotAs(OutputType.FILE);
		 // we are trying here to store screen shot with name of test cases under folder clas name
		 // like  D :\\ LoginTestCases \\ checkBlankUserName.jpg
		 
		 // Now get Test class name. result parameter of this method will help us 
		 	
		    String testClassName = result.getMethod().getTestClass().getName();
		 
			System.out.println(testClassName);
		
		 // Now get Test method/testcase name. result parameter of 
		 // this class will help us
		 
			String testMethodName = result.getMethod().getMethodName();
			 
			System.out.println(testMethodName);
			
		 try {
			FileUtils.copyFile(scrFile, new File("D:\\"+testClassName+"\\"+testMethodName+".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
			
		 
		 
		 
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("onTestSkipped");
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("onTestFailedButWithinSuccessPercentage");
	}

	@Override
	public void onStart(ITestContext context) {
		System.out.println("onStart");
	}

	@Override
	public void onFinish(ITestContext context) {
		System.out.println("onFinish");
	}

}
