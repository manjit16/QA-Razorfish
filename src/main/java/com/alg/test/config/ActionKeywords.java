package com.alg.test.config;

import org.openqa.selenium.WebDriver;

import com.alg.test.utility.ExcelUtils;
import com.alg.test.utility.Log;

public class ActionKeywords extends BasePage{

	public WebDriver launchBrowser(ExcelUtils xUtils, String sSheetName, int iTestCaseRowNum, int iTestStepRowNum){
		String sBrowser = "";
		WebDriver wDriver = null;

		sBrowser = xUtils.getCellData(iTestCaseRowNum, Constants.iBROWSERCOLUMN, Constants.sTESTEXECUTORSHEET);

		try{
			wDriver = baseLaunchLocalBrowser(sBrowser); 
		}catch(Exception e){
			Log.error("Error launching browser | Exception Description - " + e.getMessage());
			wDriver = null;
		}

		return wDriver;
	}

	public String redirectBrowser(WebDriver wDriver, ExcelUtils xUtils, String sSheetName, int iTestCaseRowNum, int iTestStepRowNum){
		String sRedirectUrlResult = "Success";
		String sUrl = "";

		try{
			sUrl = xUtils.getCellData(iTestStepRowNum, Constants.iUIINPUTSTARTCOLUMN, sSheetName);
			Log.info("Redirecting to url - " + sUrl);
			baseRedirectBrowser(wDriver, sUrl);
		}catch(Exception e){
			sRedirectUrlResult = "Error";
			Log.error("Error redirecting browser | Exception Description - " + e.getMessage());
		}

		return sRedirectUrlResult;
	}

	public String closeBrowser(WebDriver wDriver, ExcelUtils xUtils, String sSheetName, int iTestCaseRowNum, int iTestStepRowNum){
		String sResult = "Success";

		try{
			Log.info("Closing the browser");
			closeDriver(wDriver);
		}catch(Exception e){
			sResult = "Error";
			Log.error("Error closing the browser");
		}

		return sResult;
	}

	public String enterData(WebDriver wDriver, ExcelUtils xUtils, String sSheetName, int iTestCaseRowNum, int iTestStepRowNum){
		String sResult = "Success";
		String sData = "";

		try{
			sData = xUtils.getCellData(iTestStepRowNum, Constants.iUIINPUTSTARTCOLUMN, sSheetName);
			if(sData.length()>0){
				Log.info("Entering data - " + sData);
				baseEnterData(wDriver, sData, getLocator(iTestStepRowNum, xUtils, sSheetName), 5);
			}else{
				Log.info("Skipped entering data");
			}
		}catch(Exception e){
			sResult = "Error";
			Log.error("Error entering data | Exception Description - " + e.getMessage());
		}

		return sResult;
	}

	public String clickElement(WebDriver wDriver, ExcelUtils xUtils, String sSheetName, int iTestCaseRowNum, int iTestStepRowNum){
		String sResult = "Success";

		try{
			Log.info("Clicking on a webelement");
			baseClickElement(wDriver, getLocator(iTestStepRowNum, xUtils, sSheetName), 5);
		}catch(Exception e){
			sResult = "Error";
			Log.error("Error while clicking on a webelement | Exception Description - " + e.getMessage());
		}

		return sResult;
	}

	public String waitForSeconds(WebDriver wDriver, ExcelUtils xUtils, String sSheetName, int iTestCaseRowNum, int iTestStepRowNum){
		String sResult = "Success";
		int iWaitSeconds = 0;

		try{
			iWaitSeconds = Integer.parseInt(xUtils.getCellData(iTestStepRowNum, Constants.iUIINPUTSTARTCOLUMN, sSheetName));
			Log.info("Force wait for " + iWaitSeconds + " seconds");
			waitInternalSeconds(iWaitSeconds*1000);
		}catch(Exception e){
			sResult = "Error";
			Log.error("Error while force wait");
		}

		return sResult;
	}

	public String getErrorText(WebDriver wDriver, ExcelUtils xUtils, String sSheetName, int iTestCaseRowNum, int iTestStepRowNum){
		String sResult = "Init";

		try{
			Log.info("Reading data from a webelement");
			sResult = baseGetErrorText(wDriver, getLocator(iTestStepRowNum, xUtils, sSheetName), 5);
			xUtils.setCellData(sResult, iTestStepRowNum, 8, sSheetName);
		}catch(Exception e){
			sResult = "Error";
			Log.error("Error while reading data from the webelement | " + e.getMessage());
		}

		return sResult;
	}

	public String verifyClear(WebDriver wDriver, ExcelUtils xUtils, String sSheetName, int iTestCaseRowNum, int iTestStepRowNum){
		String sResult = "Success";

		try{
			Log.info("Verifying whether data is cleared");
			sResult = baseVerifyClear(wDriver, getLocator(iTestStepRowNum, xUtils, sSheetName), 5);
			xUtils.setCellData(sResult, iTestStepRowNum, 8, sSheetName);
		}catch(Exception e){
			sResult = "Error";
			Log.error("Error while verifying clear data  | " + e.getMessage());
		}

		return sResult;
	}
}
