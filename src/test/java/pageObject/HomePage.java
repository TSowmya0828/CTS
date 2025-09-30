package pageObject;
 
import java.time.Duration;
import java.util.List;
 
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
 
import Utility.DataReader;
 
public class HomePage {
	WebDriver driver;
	DataReader dataReader;
	String filePath = System.getProperty("user.dir")+"\\TestData\\BikeDetails.xlsx";
	
	//Constructor
	public HomePage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//span[normalize-space()='NEW BIKES']")
	WebElement newBikesElement;
	
	@FindBy(xpath = "//a[@title='Upcoming Bikes']")
	WebElement upcomingBikesElement;
	
	@FindBy(xpath="//a[text()='Honda']")
	WebElement manufacturerElement;
	
	@FindBy (xpath = "//li[contains(@class,'modelItem')]")
	List<WebElement> allUpcomingBikes;
 
	//Navigate to the Upcoming Bikes section
	public void hoverOnMenu(){
		//Creating object for action class
		// Performing mouse Hover action
		Actions actions = new Actions (driver);
		actions.moveToElement(newBikesElement).build().perform();
		}
	
	public void selectSubMenu() {
		upcomingBikesElement.click();
	}
	
	public void validateUpcomingBikesPage() {
		String title = driver.getTitle();
	    Assert.assertEquals(title, "Upcoming Bikes in India - Check Price, Launch Date, Images and Latest News");
	}
	
	public void selectManufacturer() {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(manufacturerElement));
		((JavascriptExecutor)
				driver).executeScript("arguments[0].scrollIntoView(true);",manufacturerElement);
		try {
			manufacturerElement.click();
			System.out.println("Honda clicked normally");
		}catch(Exception e) {
			((JavascriptExecutor)
					driver).executeScript("arguments[0].click();",manufacturerElement);
			System.out.println("Honda Clicked with javascript");
		}
	}
	
	public void getBikeDetailsLessThanFourLakhs() throws Exception {	
		// Setup Excel
		dataReader = new DataReader(filePath);
		dataReader.createSheet("UpcomingBikes");
		dataReader.setCellData("UpcomingBikes", 0, 0, "Name Of Bike");
		dataReader.setCellData("UpcomingBikes", 0, 1, "Price");
		dataReader.setCellData("UpcomingBikes", 0, 2, "Launch Date");
		int counter = 1;
		for(WebElement element : allUpcomingBikes)
		{
			try {
				String priceAttr=element.getAttribute("data-price");
				if(priceAttr == null || priceAttr.isEmpty())
				{
					continue;
				}
				int price=Integer.parseInt(priceAttr);
				if(price<=400000)
				{
					String [] cardText =element.getText().split("\n");
					String name=cardText.length>0?cardText[0]:"N/A";
					String bikePrice=cardText.length>1?cardText[1]:"N/A";
					String launchDate=cardText.length>2?cardText[2]:"N/A";
					dataReader.setCellData("UpcomingBikes", counter, 0, name);
					dataReader.setCellData("UpcomingBikes", counter, 1, bikePrice);
					dataReader.setCellData("UpcomingBikes", counter, 2, launchDate);
					System.out.println("Bike: " + name + "|" + bikePrice + "|" + launchDate);
					counter++;
			}	
		}catch(Exception e) {
			System.out.println("Skipping element:" +e.getMessage());
			}
		}
		dataReader.closeBook();
	}
}