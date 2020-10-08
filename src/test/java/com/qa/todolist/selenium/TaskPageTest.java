package com.qa.todolist.selenium;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class TaskPageTest {

	private static RemoteWebDriver driver;
	

	@Before
	public void init() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\saifr\\Desktop\\Java\\Springust-master\\src\\test\\resource\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().setSize(new Dimension(1366, 768));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	
	}

	@Test
	public void Task() throws InterruptedException {
		driver.get("file:///C:/Users/saifr/Desktop/HTML/To-Do%20List/html/index.html");
		
		WebElement target;
		
		//Create task
		target = driver.findElement(By.id("myInput"));
		target.sendKeys("Shopping");
	    WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("myInput")));
		driver.findElement(By.id("createbutton")).click();
		String TaskName = driver.findElement(By.xpath("/html/body/div/table/thead/tr[2]/td[2]")).getText();
		assertThat(TaskName).isEqualTo("Shopping"); 
		
		
	    //Read task
		WebDriverWait wait0 = new WebDriverWait(driver, 10);
		wait0.until(ExpectedConditions.elementToBeClickable(By.id("readallbutton")));
		driver.findElement(By.id("readallbutton")).click();
		
		//Update task
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.id("updatebutton")).click();
		driver.switchTo().alert().sendKeys("Cleaning");
		driver.switchTo().alert().accept();
	    WebDriverWait wait1 = new WebDriverWait(driver,10);
	    wait1.until(ExpectedConditions.elementToBeClickable(By.id("updatebutton")));
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    String UpdateTaskName = driver.findElement(By.xpath("/html/body/div/table/thead/tr[2]/td[2]")).getText();
		assertThat(UpdateTaskName).isEqualTo("Cleaning"); 
	    
		//delete task
	    WebDriverWait wait2 = new WebDriverWait(driver,10);
		wait2.until(ExpectedConditions.elementToBeClickable(By.id("deletebutton")));
	    driver.findElement(By.id("deletebutton")).click();
		driver.switchTo().alert().accept();
		
		//Create task to open up option to move to task list
		target = driver.findElement(By.id("myInput"));
		target.sendKeys("Cooking");
	    WebDriverWait wait3 = new WebDriverWait(driver, 10);
		wait3.until(ExpectedConditions.elementToBeClickable(By.id("myInput")));
		driver.findElement(By.id("createbutton")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		
		//TaskListPage
		WebDriverWait wait4 = new WebDriverWait(driver,10);
		wait4.until(ExpectedConditions.elementToBeClickable(By.id("tasklistbutton")));
		driver.findElement(By.id("tasklistbutton")).click();
		driver.navigate().to("file:///C:/Users/saifr/Desktop/HTML/To-Do%20List/html/taskList.html");
	
	
		//Create task list
		target = driver.findElement(By.id("myInputTL"));
		target.sendKeys("Buy Fruits");
	    WebDriverWait wait5 = new WebDriverWait(driver, 10);
		wait5.until(ExpectedConditions.elementToBeClickable(By.id("myInputTL")));
		driver.findElement(By.id("createbuttonTL")).click();	
		String TaskListName = driver.findElement(By.xpath("/html/body/div/table/thead/tr[2]/td[2]")).getText();
		assertThat(TaskListName).isEqualTo("Buy Fruits"); 
		
		//Read task list
		WebDriverWait wait6 = new WebDriverWait(driver, 10);
		wait6.until(ExpectedConditions.elementToBeClickable(By.id("readallbuttonTL")));
		driver.findElement(By.id("readallbuttonTL")).click();
		
		//Update task list
		 driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.id("updatebuttonTL")).click();
		driver.switchTo().alert().sendKeys("Go to the store and buy fruits");
		driver.switchTo().alert().accept();
	    WebDriverWait wait7 = new WebDriverWait(driver,10);
	    wait7.until(ExpectedConditions.elementToBeClickable(By.id("updatebuttonTL")));
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    String UpdateTaskName1 = driver.findElement(By.xpath("/html/body/div/table/thead/tr[2]/td[2]")).getText();
		assertThat(UpdateTaskName1).isEqualTo("Go to the store and buy fruits"); 
		
	    //delete task list
	    WebDriverWait wait8 = new WebDriverWait(driver, 10);
		wait8.until(ExpectedConditions.elementToBeClickable(By.id("deletebuttonTL")));
	    driver.findElement(By.id("deletebuttonTL")).click();
		driver.switchTo().alert().accept();
		
		//BacktoTaskPage
		WebDriverWait wait9 = new WebDriverWait(driver, 10);
		wait9.until(ExpectedConditions.elementToBeClickable(By.id("backbuttonTL")));
		driver.findElement(By.id("backbuttonTL")).click();
		driver.navigate().to("file:///C:/Users/saifr/Desktop/HTML/To-Do%20List/html/index.html");

	}

	@After
	public void cleanUp() {
		driver.quit();
	}
}