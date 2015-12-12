package com.testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverInstance {
private static WebDriver d;
	
	public static WebDriver getDriverInstance() {
		return d;
	}
	
	public static WebDriver startDriver() {
		d = new FirefoxDriver();
		return d;
	}
	
	public static void stopDriver() {
		d.close();
	}
}
