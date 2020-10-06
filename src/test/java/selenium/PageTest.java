package selenium;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;



public class PageTest {

    private WebDriver driver;

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\saifr\\Desktop\\Java\\Springust-master\\src\\test\\resource\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1366, 768));

        
    }

    @Test
    public void test() throws InterruptedException {
    	WebElement target;
        driver.get("file:///C:/Users/saifr/Desktop/HTML/To-Do%20List/html/index.html");
        Thread.sleep(15000);

  //target= driver.findElement(By.name("search_query"));
  //target.sendKeys("Dress");
 // target.submit();
 // Thread.sleep(15000);
    }

    @After
    public void tearDown() {
        driver.close();
    }
}