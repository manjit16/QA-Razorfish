package com.alg.test.executionengine;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alg.test.config.ActionKeywords;
import com.alg.test.config.Constants;
import com.alg.test.framework.BaseTest;
import com.alg.test.utility.ExcelUtils;
import com.alg.test.utility.Log;

public class Executor extends BaseTest{
	
	public static String sResultFolder = "";
	public static String sResultFile = "DataEngine.xlsx";
	
	@BeforeClass
	public void beforeClass(){
		//Start class log
		Log.info("Start class");
		
		//Set the excel file object
		try{
			Log.info("Setting the data engine object");
			ExcelUtils.setExcelFile(sResultFile);
		}catch(Exception e){
			Log.error("Error setting the excelfile | Exception Description - " + e.getMessage());
		}
	}
	
	@AfterClass
	public void afterClass(){
		//Save the result
		try{
			Log.info("Saving the result");
			ExcelUtils.saveResults(sResultFile);
		}catch(Exception e){
			Log.error("Error saving the results | Exception Description - " + e.getMessage());
		}
	}
	
	@DataProvider(name = "TestCases", parallel=true)
	public static Object[][] dataProvider(){
		ExcelUtils xUtils = new ExcelUtils();
		Object[][] testCases = xUtils.getTestCases();
		
		return testCases;
	}
	
	@Test(dataProvider = "TestCases")
	public void testCase(String sRowNum){
		ActionKeywords aKeywords = new ActionKeywords();
		Method method[] = aKeywords.getClass().getMethods();
		WebDriver wDriver = null;
		ExcelUtils xUtils = new ExcelUtils();
		String sSheetName = "";
		int iTestCaseStartNum = 0;
		int iTestCaseEndNum = 0;
		String sKeyword= "";
		Object oValue;
		String sActualData = "";
		String sStepResult = "";
		String sScreenshot = "";
		String sTestCaseResult = "Pass";
		String sTestCaseNumber = "";
		String sTestCaseName = "";
		int iTestCaseNum = 0;
		
		//Get the test case row number
		iTestCaseNum = Integer.parseInt(sRowNum);
		
		//Get the test case number, test case name and sheet name where test steps of that test case is defined
		sTestCaseNumber = xUtils.getCellData(iTestCaseNum, Constants.iTESTCASENUMBERCOLUMN, Constants.sTESTEXECUTORSHEET);
		sTestCaseName = xUtils.getCellData(iTestCaseNum, Constants.iTESTCASECOLUMN, Constants.sTESTEXECUTORSHEET);
		sSheetName = xUtils.getCellData(iTestCaseNum, Constants.iSHEETNAMECOLUMN, Constants.sTESTEXECUTORSHEET);
		
		//Check if run mode is yes
		if(xUtils.getCellData(iTestCaseNum, Constants.iRUNMODECOLUMN, Constants.sTESTEXECUTORSHEET).contentEquals("Yes")){
			Log.startTestCase(sTestCaseNumber);
			
			//Get start and end row of the test steps of the test case
			try{
				iTestCaseStartNum = xUtils.getTestCaseStartNum(sSheetName, sTestCaseName);
				iTestCaseEndNum = xUtils.getTestCaseEndNum(sSheetName, sTestCaseName);
			}catch(Exception e){
				Log.error("Error while fecthing start and end row num of the test case");
			}
			
			//Loop through the test steps
			outerloop:
				for(int i=iTestCaseStartNum; i<=iTestCaseEndNum; i++){					
					//Code for populating actual data
					sKeyword = xUtils.getCellData(i, Constants.iKEYWORDCOLUMN, sSheetName);
					for(int j=0;j<method.length;j++){
						if(method[j].getName().equals("launchBrowser") && sKeyword.equals("launchBrowser")){
							try{
								//Execute the test step and get the actual data
								oValue = method[j].invoke(aKeywords, xUtils, sSheetName, iTestCaseNum, i);
								wDriver = (WebDriver) oValue;
								
								//Populate the actual data
								if(wDriver != null){
									xUtils.setCellData("Success", i, Constants.iACTUALDATACOLUMN, sSheetName);
								}else{
									xUtils.setCellData("Error", i, Constants.iACTUALDATACOLUMN, sSheetName);
								}
								
								//Compare the results
								sStepResult = xUtils.compareResults(sSheetName, i, Constants.iEXPECTEDDATACOLUMN, Constants.iACTUALDATACOLUMN, Constants.iRESULTCOLUMN);
								
								//Take screenshot if test step has failed
								//Set test case result as fail
								if(sStepResult.contentEquals("Fail") || sStepResult.contentEquals("Error")){
									//sScreenshot = aKeywords.captureScreenshot(wDriver, xUtils, sSheetName, iTestCaseNum, i, sResultFolder);
									xUtils.setCellData(sScreenshot, i, Constants.iFILENAMECOLUMN, sSheetName);
									
									sTestCaseResult = "Fail";
									break outerloop;
								}
							}catch(Exception e){
								Log.error("Error while executing actual data step | Exception Desc - " + e.getMessage());
							}
							
							break;
						} else if(method[j].getName().equals(sKeyword)){
							try{
								//Execute the test step and get the actual data
								oValue = method[j].invoke(aKeywords, wDriver, xUtils, sSheetName, iTestCaseNum, i);
								sActualData = (String) oValue;
								
								//Populate the actual data
								xUtils.setCellData(sActualData, i, Constants.iACTUALDATACOLUMN, sSheetName);
								
								//Compare the results
								sStepResult = xUtils.compareResults(sSheetName, i, Constants.iEXPECTEDDATACOLUMN, Constants.iACTUALDATACOLUMN, Constants.iRESULTCOLUMN);
								
								//Take screenshot if test step has failed
								//Set test case result as fail
								if(sStepResult.contentEquals("Fail") || sStepResult.contentEquals("Error")){
									//sScreenshot = aKeywords.captureScreenshot(wDriver, xUtils, sSheetName, iTestCaseNum, i, sResultFolder);
									xUtils.setCellData(sScreenshot, i, Constants.iFILENAMECOLUMN, sSheetName);
									
									sTestCaseResult = "Fail";
									break outerloop;
								}
							}catch(Exception e){
								Log.error("Error while executing actual data step | Exception Desc - " + e.getMessage());
							}
							
							break;
						}
					}
				}
			//Populate the result of the test case
			try{
				xUtils.setCellData(sTestCaseResult, iTestCaseNum, Constants.iTESTCASERESULTCOLUMN, Constants.sTESTEXECUTORSHEET);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			Log.endTestCase(sTestCaseNumber);
			
			if(sTestCaseResult.contentEquals("Fail")){
				assert false;
			}
		}else{
			try{
				xUtils.setCellData("Skipped", iTestCaseNum, Constants.iTESTCASERESULTCOLUMN, Constants.sTESTEXECUTORSHEET);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}