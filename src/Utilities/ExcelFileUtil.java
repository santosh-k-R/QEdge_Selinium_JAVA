package Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelFileUtil {

	Workbook wb;
	//constructor to read path of excel file
	public ExcelFileUtil(String excelpath)throws Throwable
	{
		FileInputStream fi=new FileInputStream(excelpath);
		wb=WorkbookFactory.create(fi);
	}
	//count nor of rows in a sheet
	public int rowCount(String sheetname)
	{
		return wb.getSheet(sheetname).getLastRowNum() ;
	}
	//count nor of columns in a row
	public int colCount(String sheetname)
	{
		return wb.getSheet(sheetname).getRow(0).getLastCellNum();
	}
	//get data from a cell
	public String getCellData(String sheetname,int row,int column)
	{
		String data="";
		if(wb.getSheet(sheetname).getRow(row).getCell(column).getCellType()==Cell.CELL_TYPE_NUMERIC)
		{
			int cellData=(int) wb.getSheet(sheetname).getRow(row).getCell(column).getNumericCellValue();
			data=String.valueOf(cellData);
		}
		else
		{
			data=wb.getSheet(sheetname).getRow(row).getCell(column).getStringCellValue();
		}
		return data;
	}

	//method for writing  results
	public void setCellData(String sheetname,int row,int column,String status,String resultexcel) throws Throwable
	{
		//get sheet form wb
		Sheet ws=wb.getSheet(sheetname);
		//get row from sheet
		Row rownum=ws.getRow(row);
		//cretae cell
		Cell cell=rownum.createCell(column);
		cell.setCellValue(status);
		if(status.equalsIgnoreCase("Pass"))
		{
			//create cell style
			CellStyle style=wb.createCellStyle();
			//create font
			Font font=wb.createFont();
			font.setColor(IndexedColors.GREEN.getIndex());
			//apply bold to the text
			font.setBold(true);
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			rownum.getCell(column).setCellStyle(style);
		}
		else if(status.equalsIgnoreCase("Fail"))
		{
			//create cell style
			CellStyle style=wb.createCellStyle();
			//create font
			Font font=wb.createFont();
			//Apply color To The Text
			font.setColor(IndexedColors.RED.getIndex());
			//Apply Bold To The Text
			font.setBold(true);
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			style.setFont(font);
			rownum.getCell(column).setCellStyle(style);
		}
		else if(status.equalsIgnoreCase("Not Executed"))
		{
			//create cell style
			CellStyle style=wb.createCellStyle();
			//create font
			Font font=wb.createFont();
			//Apply color To The Text
			font.setColor(IndexedColors.BLUE.getIndex());
			//Apply Bold To The Text
			font.setBold(true);
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			style.setFont(font);
			rownum.getCell(column).setCellStyle(style);
		}
		FileOutputStream fo=new FileOutputStream(resultexcel);
		wb.write(fo);
	}
} 
