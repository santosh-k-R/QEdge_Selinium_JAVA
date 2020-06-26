package DriverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CommonFunLibrary.FunctionLibrary;
import Utilities.ExcelFileUtil;

public class DriverScript {

	WebDriver driver;
	ExtentReports report;
	ExtentTest test;
	String inputpath="F:\\QEdgeWorksace\\ERP_StockAccounting\\TestInput\\InputSheet.xlsx";
	String outputpath="F:\\QEdgeWorksace\\ERP_StockAccounting\\TestOutput\\Hybrid.xlsx";
	public void startTest()throws Throwable
	{
		//creating reference object for excel util methods
		ExcelFileUtil excel=new ExcelFileUtil(inputpath);
		//iterating all row in MasterTestCases sheet
		for(int i=1;i<=excel.rowCount("MasterTestCases");i++)
		{
			if(excel.getCellData("MasterTestCases", i, 2).equalsIgnoreCase("Y"))
			{
				//store module name into TCModule 
				String tcModule=excel.getCellData("MasterTestCases", i, 1);
				//generate report under project
				report=new ExtentReports("./ExtentReports/"+tcModule+FunctionLibrary.generateDate()+".html");
				//iterate all rows in TCModule sheet
				for(int j=1;j<=excel.rowCount(tcModule);j++)
				{
					test=report.startTest(tcModule);	
					//read all columns in 	TCModule
					String description=excel.getCellData(tcModule, j, 0);
					String functionName=excel.getCellData(tcModule, j, 1);
					String locatorType=excel.getCellData(tcModule, j, 2);
					String locatorValue=excel.getCellData(tcModule, j, 3);
					String testData=excel.getCellData(tcModule, j, 4);
					try
					{
						//call functions
						if(functionName.equalsIgnoreCase("startBrowser"))
						{
							driver=FunctionLibrary.startBrowser();
							test.log(LogStatus.INFO, description);

						}
						else if(functionName.equalsIgnoreCase("openApplication"))
						{
							FunctionLibrary.openApplication(driver);
							test.log(LogStatus.INFO, description);
						}
						else if(functionName.equalsIgnoreCase("waitForElement"))
						{
							FunctionLibrary.waitForElement(driver, locatorType, locatorValue, testData);
							test.log(LogStatus.INFO, description);
						}
						else if(functionName.equalsIgnoreCase("typeAction"))
						{
							FunctionLibrary.typeAction(driver, locatorType, locatorValue, testData);
							test.log(LogStatus.INFO, description);
						}
						else if(functionName.equalsIgnoreCase("clickAction"))
						{
							FunctionLibrary.clickAction(driver, locatorType, locatorValue);
							test.log(LogStatus.INFO, description);
						}
						else if(functionName.equalsIgnoreCase("closeBrowser"))
						{
							FunctionLibrary.closeBrowser(driver);
							test.log(LogStatus.INFO, description);
						}
						else if(functionName.equalsIgnoreCase("captureData"))
						{
							FunctionLibrary.captureData(driver, locatorType, locatorValue);
							test.log(LogStatus.INFO, description);
						}
						else if(functionName.equalsIgnoreCase("tableValidation"))
						{
							FunctionLibrary.tableValidation(driver, testData);
							test.log(LogStatus.INFO, description);
						}
						else if(functionName.equalsIgnoreCase("stockCategories"))
						{
							FunctionLibrary.stockCategories(driver);
							test.log(LogStatus.INFO, description);
						}
						else if(functionName.equalsIgnoreCase("stockValidation"))
						{
							FunctionLibrary.stockValidation(driver, testData);
							test.log(LogStatus.INFO, description);
						}
						//write as pass into status column TCModule
						excel.setCellData(tcModule, j, 5, "Pass", outputpath);
						test.log(LogStatus.PASS, description);
						//write as pass into MaterTestCases sheet
						excel.setCellData("MasterTestCases", i, 3, "Pass", outputpath);
					}
					catch(Exception e)
					{
						//take screen shot and store
						File screen=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						//copy screen shot into your project
						FileUtils.copyFile(screen, new File("./Screens/"+tcModule+FunctionLibrary.generateDate()+".png"));
						//write as Fail into status column TCModule	
						excel.setCellData(tcModule, j, 5, "Fail", outputpath);
						test.log(LogStatus.FAIL, description);
						//write as Fail into MaterTestCases sheet
						excel.setCellData("MasterTestCases", i, 3, "Fail", outputpath);
						System.out.println(e.getMessage());
					}
				}
			}
			else
			{
				//write as not executed into status column in MasterTestCases
				excel.setCellData("MasterTestCases", i, 3, "Not Executed",outputpath);
			}
		}

	}




}
