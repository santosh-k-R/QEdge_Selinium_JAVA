package CommonFunLibrary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Utilities.PropertyFileUtil;

public class FunctionLibrary {
	public static WebDriver driver;
	//method for launching browser
	public static WebDriver startBrowser() throws Throwable
	{
		if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("chrome"))
		{
			System.setProperty("webdriver.chrome.driver","F:\\QEdgeWorksace\\ERP_StockAccounting\\CommonDrivers\\chromedriver.exe");
			driver=new ChromeDriver();
		}
		else if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("firefox"))
		{
			System.setProperty("webdriver.gecko.driver", "F:\\QEdgeWorksace\\ERP_StockAccounting\\CommonDrivers\\geckodriver.exe");
			driver=new FirefoxDriver();
		}
		else
		{
			System.out.println("browser key value not matching");
		}
		return driver;
	}
	//method for launching URL
	public static void openApplication(WebDriver driver) throws Throwable
	{
		driver.get(PropertyFileUtil.getValueForKey("Url"));
		driver.manage().window().maximize();
	}
	//method for wait  for elements
	public static void waitForElement(WebDriver driver,String locatortype,String locatorvalue,String waittime)
	{
		WebDriverWait myWait=new WebDriverWait(driver, Integer.parseInt(waittime));
		if(locatortype.equalsIgnoreCase("id"))
		{
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorvalue)));
		}
		else if(locatortype.equalsIgnoreCase("name"))
		{
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorvalue)));
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorvalue)));	
		}
		else
		{
			System.out.println("Waitforelement method is not matching with any locator");
		}
	}
	//method for type action
	public static void typeAction(WebDriver driver,String locatortype,String locatorvalue,String testdata)
	{
		if(locatortype.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(locatorvalue)).clear();
			driver.findElement(By.id(locatorvalue)).sendKeys(testdata);
		}
		else if(locatortype.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(locatorvalue)).clear();
			driver.findElement(By.name(locatorvalue)).sendKeys(testdata);
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(locatorvalue)).clear();
			driver.findElement(By.xpath(locatorvalue)).sendKeys(testdata);
		}
		else
		{
			System.out.println("Type action method unable to execute");
		}
	}
	//method for  click element action
	public static void clickAction(WebDriver driver,String locatortype,String locatorvalue)
	{
		if(locatortype.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(locatorvalue)).sendKeys(Keys.ENTER);
		}
		else if(locatortype.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(locatorvalue)).click();
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(locatorvalue)).click();	
		}
		else
		{
			System.out.println("Unable to execute clickAction");
		}
	}
	//method  to close browser
	public static void closeBrowser(WebDriver driver)
	{
		driver.close();
	}
	//java time stamp
	public static String generateDate()
	{
		Date d=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY_MM_dd_ss");
		return sdf.format(d);
	}
	//method for capture data 
	public static void captureData(WebDriver driver,String locatortype,String locatorvalue) throws Throwable
	{
		String snumber="";
		if(locatortype.equalsIgnoreCase("id"))
		{
			snumber=driver.findElement(By.id(locatorvalue)).getAttribute("value");
		}
		else if(locatortype.equalsIgnoreCase("name"))
		{
			snumber=driver.findElement(By.name(locatorvalue)).getAttribute("value");
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			snumber=driver.findElement(By.xpath(locatorvalue)).getAttribute("value");
		}
		FileWriter fw=new FileWriter("F:\\QEdgeWorksace\\ERP_StockAccounting\\CaptureData\\supplier.txt");
		BufferedWriter bw= new BufferedWriter(fw);
		bw.write(snumber);
		bw.flush();
	}
	//method for table validation
	public static void tableValidation(WebDriver driver,String columndata) throws Throwable
	{
		FileReader fr= new FileReader("F:\\QEdgeWorksace\\ERP_StockAccounting\\CaptureData\\supplier.txt");
		BufferedReader br=new BufferedReader(fr);
		String exp_data=br.readLine();
		//cconvert column data in to integer
		int column=Integer.parseInt(columndata);
		if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("searchtextbox"))).isDisplayed())
		{
			//if not displayed search text box
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("searchpanel"))).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("searchtextbox"))).clear();
			Thread.sleep(4000);
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("searchtextbox"))).sendKeys(exp_data);
			Thread.sleep(4000);
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("searchbutton"))).click();
			WebElement table=driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("stable")));
			//get collection of rows in table
			List<WebElement> rows=table.findElements(By.tagName("tr"));
			for(int i=1;i<rows.size();i++)
			{
				String act_data=driver.findElement(By.xpath("//table[@id='tbl_a_supplierslist']/tbody/tr["+i+"]/td["+column+"]")).getText();
				Thread.sleep(4000);
				Assert.assertEquals(act_data, exp_data,"supplier not matching");
				System.out.println(act_data+"  "+exp_data);
				break;
			}
		}
	}
	//method for stock items
	public static void stockCategories(WebDriver driver) throws Throwable
	{
		Actions ac=new Actions(driver);
		ac.moveToElement(driver.findElement(By.linkText("Stock Items"))).perform();
		Thread.sleep(5000);
		ac.moveToElement(driver.findElement(By.xpath("//li[@id='mi_a_stock_categories']//a")));
		ac.click().build().perform();
		Thread.sleep(5000);
	}
	public static void stockValidation(WebDriver driver,String Exp_data)throws Throwable
	{
	if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("searchtextbox1"))).isDisplayed())
		//if not displayed search textbox archtextbox1
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("searchpanel1"))).click();
	Thread.sleep(5000);
	//clear text in textbox
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("searchtextbox1"))).clear();
	Thread.sleep(5000);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("searchtextbox1"))).sendKeys(Exp_data);
	Thread.sleep(5000);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("searchbutton1"))).click();
	Thread.sleep(5000);
	//validate in table
	WebElement table=driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("cattable")));
	//get collection of rows in a table
	List<WebElement>rows=table.findElements(By.tagName("tr"));
	System.out.println("no of rows are::"+rows.size());
	for(int i=1;i<rows.size();i++)
	{
	//capture cell data 
	String Act_Data=driver.findElement(By.xpath("//table[@id='tbl_a_stock_categorieslist']/tbody/tr["+i+"]/td[4]/div/span/span")).getText();
	System.out.println(Act_Data+"      "+Exp_data);
	Assert.assertEquals(Act_Data, Exp_data,"Item is not found in table");
	break;
	}
	}
}
