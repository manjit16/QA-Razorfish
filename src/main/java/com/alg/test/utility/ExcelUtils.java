package com.alg.test.utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alg.test.config.Constants;

public class ExcelUtils {
	
	private static XSSFWorkbook xExcelWBook;
	
	public static void setExcelFile(String sPath) throws Exception{
		FileInputStream excelFile = new FileInputStream(sPath);
		xExcelWBook = new XSSFWorkbook(excelFile);
	}
	
	public int getRowCount(String sSheetName) throws Exception{
		XSSFSheet xExcelWSheet;
		int iNumber = 0;
		
		xExcelWSheet = xExcelWBook.getSheet(sSheetName);
		iNumber = xExcelWSheet.getLastRowNum() + 1;
		
		return iNumber;		
	}
	
	public int getTestCaseStartNum(String sSheetName, String sTestCaseName) throws Exception{
		int iRowCount=0;
		int iTestCaseStartNum=0;
		
		iRowCount = getRowCount(sSheetName);
		
		for(int i=0;i<=iRowCount;i++){
			if(sTestCaseName.equals(getCellData(i, 0, sSheetName)) && (getCellData(i, 1, sSheetName)).equalsIgnoreCase("Begin")){
				iTestCaseStartNum = i+2;
				break;
			}
		}
		
		return iTestCaseStartNum;
	}
	
	public int getTestCaseEndNum(String sSheetName, String sTestCaseName) throws Exception{
		int iRowCount=0;
		int iTestCaseEndNum=0;
		
		iRowCount = getRowCount(sSheetName);
		
		for(int i=0;i<=iRowCount;i++){
			if(sTestCaseName.equals(getCellData(i, 0, sSheetName)) && (getCellData(i, 1, sSheetName)).equalsIgnoreCase("End")){
				iTestCaseEndNum = i-1;
				break;
			}
		}
		
		return iTestCaseEndNum;
	}
	
	public String getCellData(int iRowNum, int iColNum, String sSheetName){
		Cell cCell;
		XSSFRow xRow;
		XSSFSheet xExcelWSheet;
		String sCellData = "";
		
		try{
			xExcelWSheet = xExcelWBook.getSheet(sSheetName);
			xRow = xExcelWSheet.getRow(iRowNum);
			cCell = xRow.getCell(iColNum);
			sCellData = cCell.getStringCellValue();
		}catch(Exception e){
			sCellData = "";
		}
		
		return sCellData;
	}
	
	public void setCellData(String sResult, int iRowNum, int iColNum, String sSheetName) throws Exception{
		Cell cCell;
		XSSFRow xRow;
		XSSFSheet xExcelSheet;
		
		xExcelSheet = xExcelWBook.getSheet(sSheetName);
		xRow = xExcelSheet.getRow(iRowNum);
		cCell = xRow.getCell(iColNum, xRow.RETURN_BLANK_AS_NULL);
		if(cCell == null){
			cCell = xRow.createCell(iColNum);
			cCell.setCellValue(sResult);
		}else{
			cCell.setCellValue(sResult);
		}
	}
	
	public static void saveResults(String sFileName) throws Exception{
		FileOutputStream fileOut = new FileOutputStream(sFileName);
		xExcelWBook.write(fileOut);
		fileOut.close();
	}
	
	public String compareResults(String sSheetName, int iRowNum, int iExpectedCell, int iActualCell, int iResultCell){
		String sCompareResult = "Pass";
		String sExpectedData = "";
		String sActualData = "";
		
		try{
			sExpectedData = getCellData(iRowNum, iExpectedCell, sSheetName);
			sActualData = getCellData(iRowNum, iActualCell, sSheetName);
			if(!sExpectedData.contentEquals("NoCompare")){
				if(sExpectedData.contentEquals(sActualData)){
					setCellData(sCompareResult, iRowNum, iResultCell, sSheetName);
				}else{
					sCompareResult = "Fail";
					setCellData(sCompareResult, iRowNum, iResultCell, sSheetName);
				}
			}
		}catch(Exception e){
			sCompareResult = "Error";
			Log.error("Class ExcelUtils | Method compareResults | Exception Description - " + e.getMessage());
		}
		
		return sCompareResult;
	}
	
	public Object[][] getTestCases(){
		String[][] tabArray = null;
		int iTotalRows = 0;
		int iActualTestCaseCount = 0;
		XSSFSheet xExcelWSheet;
		
		xExcelWSheet = xExcelWBook.getSheet(Constants.sTESTEXECUTORSHEET);
		iTotalRows = xExcelWSheet.getLastRowNum();
		
		for(int i=Constants.iTESTCASESTARTROW; i<=iTotalRows; i++){
			if(getCellData(i, Constants.iTESTCASECOLUMN, Constants.sTESTEXECUTORSHEET).length()>2){
				iActualTestCaseCount = iActualTestCaseCount + 1;
			}
		}
		
		tabArray = new String[iActualTestCaseCount][1];
		iTotalRows = Constants.iTESTCASESTARTROW + iActualTestCaseCount;
		
		for(int i=Constants.iTESTCASESTARTROW; i<iTotalRows; i++){
			tabArray[i-Constants.iTESTCASESTARTROW][0] = i + "";
		}
		
		return tabArray;
	}
}
