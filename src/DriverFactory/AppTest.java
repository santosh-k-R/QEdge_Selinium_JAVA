package DriverFactory;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import CommonFunLibrary.FunctionLibrary;

public class AppTest {
	@Test
	public void kickStart()
	{	
		try {
			DriverScript ds=new DriverScript();
			ds.startTest();
		}catch(Throwable t)
		{
			System.out.println(t.getMessage());
		}
	}
	
	public static void main(String[] args) throws Throwable {
		FunctionLibrary fl=new FunctionLibrary();
		WebDriver driver=fl.startBrowser();
		fl.closeBrowser(driver);
	}
}
