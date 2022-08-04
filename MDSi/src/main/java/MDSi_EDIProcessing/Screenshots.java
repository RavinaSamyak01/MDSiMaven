package MDSi_EDIProcessing;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


public class Screenshots extends MDSiOrderCreation {
	
	public static void takeSnapShot(WebDriver webdriver,String fileWithPath) throws Exception
	{

        TakesScreenshot scrShot =((TakesScreenshot)webdriver); //Convert web driver object to TakeScreenshot
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE); //Call getScreenshotAs method to create image file
        File DestFile=new File(fileWithPath);  				   //Move image file to new destination
        FileUtils.copyFile(SrcFile, DestFile);				   //Copy file at destination

    }

}