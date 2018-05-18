package com.alg.test.utility;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class Utils {
	
	public static String dateInFormat(Date date, String format){
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
	public static Properties openProperties(String sPropFile) throws Exception{
		FileInputStream propFileInp = new FileInputStream(sPropFile);
		Properties propFile = new Properties();
		
		propFile.load(propFileInp);
		propFileInp.close();
		
		return propFile;
	}
	
	public static void copyFileToDirectory(String sFileName, String sFolder) throws Exception{
		File fSourceFile;
		File fDestination;
		
		fSourceFile = new File(sFileName);
		fDestination = new File(sFolder);
		
		FileUtils.copyFileToDirectory(fSourceFile, fDestination);
	}
	
	public static String createDirectory(String sPath, String sAppender){
		String sFolderName = sPath + sAppender;
		String returnType = "DirectoryNotCreated";
		
		File sFile = new File(sFolderName);
		
		if(!sFile.exists()){
			if(sFile.mkdir()){
				returnType = sFolderName + "\\";
				return returnType;
			}else{
				return "DirectoryNotCreated";
			}
		}
		
		return returnType;
	}
}
