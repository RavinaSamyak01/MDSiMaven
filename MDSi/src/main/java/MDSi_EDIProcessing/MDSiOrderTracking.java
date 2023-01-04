package MDSi_EDIProcessing;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class MDSiOrderTracking extends StartUp {

	// static WebDriver driver;
	static StringBuilder msg = new StringBuilder();
	static double TrackingTime;

	@Test
	public void mdSiOrderTracking() throws Exception {
		long start, end;

		WebDriverWait wait = new WebDriverWait(driver, 50);
		Actions act = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		String Env = storage.getProperty("Env");
		System.out.println("Env " + Env);

		String baseUrl = null;
		if (Env.equalsIgnoreCase("Pre-Prod")) {
			baseUrl = storage.getProperty("PREPRODURLOrderTrack");
		} else if (Env.equalsIgnoreCase("STG")) {
			baseUrl = storage.getProperty("STGURLOrderTrack");
		} else if (Env.equalsIgnoreCase("DEV")) {
			baseUrl = storage.getProperty("DEVURLOrderTrack");
		}
		Thread.sleep(2000);
		driver.get(baseUrl);

		try {
			wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(@class,'body-content')]")));
			// Read data from Excel
			File src = new File(".\\src\\main\\resources\\MDSiTestResult.xlsx");
			FileInputStream fis = new FileInputStream(src);
			Workbook workbook = WorkbookFactory.create(fis);
			Sheet sh1 = workbook.getSheet("Sheet1");

			js.executeScript("window.scrollBy(0,-250)");
			Thread.sleep(2000);

			// Default size
			Dimension currentDimension = driver.manage().window().getSize();
			int height = currentDimension.getHeight();
			int width = currentDimension.getWidth();
			System.out.println("Current height: " + height);
			System.out.println("Current width: " + width);
			System.out.println("window size==" + driver.manage().window().getSize());

			Dimension newDimension = new Dimension(745, 600);
			driver.manage().window().setSize(newDimension);
			Thread.sleep(2000);

			WebElement JobIDLink = driver.findElement(By.id("MainContent_HyperLinkJobID"));
			act.moveToElement(JobIDLink).build().perform();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("MainContent_HyperLinkJobID")));
			wait.until(ExpectedConditions.elementToBeClickable(By.id("MainContent_HyperLinkJobID")));
			act.moveToElement(JobIDLink).click().perform();

			wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(@class,'body-content')]")));

			js.executeScript("window.scrollBy(0,-250)");
			Thread.sleep(2000);

			for (int i = 1; i < 4; i++) {
				DataFormatter formatter = new DataFormatter();
				String JobID = formatter.formatCellValue(sh1.getRow(i).getCell(1));
				System.out.println("Job Id is==" + JobID);
				msg.append("Job Id is==" + JobID + "\n");
				WebElement JobIDInput = driver.findElement(By.id("MainContent_txtJobID"));
				act.moveToElement(JobIDInput).build().perform();
				Thread.sleep(2000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("MainContent_txtJobID")));
				wait.until(ExpectedConditions.elementToBeClickable(By.id("MainContent_txtJobID")));
				driver.findElement(By.id("MainContent_txtJobID")).clear();
				driver.findElement(By.id("MainContent_txtJobID")).sendKeys(JobID);

				driver.findElement(By.id("MainContent_txtUserName")).clear();
				driver.findElement(By.id("MainContent_txtUserName")).sendKeys("MDSI_WS");

				driver.findElement(By.id("MainContent_txtPassword")).clear();
				driver.findElement(By.id("MainContent_txtPassword")).sendKeys("MDSI_WS_14");

				driver.findElement(By.id("MainContent_ButtonTrackOrder")).click();
				// --Start time
				start = System.nanoTime();
				Thread.sleep(6000);

				Screenshots.takeSnapShot(driver, ".\\src\\main\\resources\\Screenshots\\MDSiTracking.jpg");
				Thread.sleep(3000);
				String Job = driver.findElement(By.id("MainContent_lblTrackOrderresult")).getText();
				System.out.println("MDSi Track Order DONE !");
				end = System.nanoTime();
				TrackingTime = (end - start) * 1.0e-9;
				System.out.println("Tracking Time (in Seconds) = " + TrackingTime);
				msg.append("Tracking Time (in Seconds) = " + TrackingTime + "\n");
				msg.append("Response :" + "\n" + Job + "\n\n");

			}
		} catch (Exception E) {
			msg.append("Something went wrong" + "\n");

		}
		Env = storage.getProperty("Env");
		String subject = "Selenium Automation Script: " + Env + " MDSi_EDI - Shipment Tracking";
		String SS = ".\\src\\TestFiles\\MDSiTracking.jpg";
//		/// asharma@samyak.com,pgandhi@samyak.com,kunjan.modi@samyak.com,pdoshi@samyak.com
		try {
			Email.sendMail("ravina.prajapati@samyak.com,asharma@samyak.com,parth.doshi@samyak.com,saurabh.jain@samyak.com", subject,
					msg.toString(), SS);
		} catch (Exception ex) {
			Logger.getLogger(MDSiOrderCreation.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
