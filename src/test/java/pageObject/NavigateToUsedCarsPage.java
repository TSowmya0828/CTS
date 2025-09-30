package pageObject;
 
import java.time.Duration;
import java.util.List;
 
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
 
import Utility.DataReader;
import factory.BaseClass;
 
 
public class NavigateToUsedCarsPage extends BaseClass{
 
	WebDriver driver;
	DataReader dataReader;
	String filePath = System.getProperty("user.dir")+"\\TestData\\UsedCar.xlsx";
	
	//Constructor
	public NavigateToUsedCarsPage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@FindBy(xpath="//span[normalize-space()='MORE']")
	WebElement moreMenu;
	
	@FindBy (xpath = "//a[normalize-space()='Used Cars']")
	WebElement usedCarElement;
	
	@FindBy (xpath = "//div[@id='city-popup']")
	WebElement cityPopupElement;
	
	@FindBy (xpath = "//input[@id='gs_input5']")
	WebElement popupInputElement;

	@FindBy (xpath = "//input[@id='usedCarCity']")
	WebElement inputCityElement;
	
	@FindBy (xpath="//h1[@id='usedcarttlID']")
	WebElement headLineTextElement;
	
	@FindBy (xpath="//h1[contains(text(),'Chennai')]")
	WebElement waitElement;
	
	@FindBy (xpath = "//a[text()='Chennai']")
	WebElement cityDropDownElement;
	
	@FindBy (xpath = "//ul[contains(@class,'usedCarMakeModelList')]//li//span//input")
	List<WebElement> popularModelsCheckBoxList;
	
	public void clickOnUsedCars() {
		try {
			WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(15));
 
			wait.until(ExpectedConditions.visibilityOf(moreMenu));
			Actions actions = new Actions(driver);
			actions.moveToElement(moreMenu).perform();
			
			wait.until(ExpectedConditions.elementToBeClickable(usedCarElement));
			JavascriptExecutor js=(JavascriptExecutor)driver;
			js.executeScript("arguments[0].scrollIntoView(true);",usedCarElement);
			js.executeScript("arguments[0].click();",usedCarElement);
			wait.until(ExpectedConditions.titleContains("Used Cars"));
			System.out.println("Navigated to Used cars page");
		}catch(Exception e) {
			System.out.println("Failed to click Used Cars:"+e.getMessage());
		}
	}
	public String getTitleOfPage() {
		return driver.getTitle();
	}
	public String getHeadlineOfPage() {
		return headLineTextElement.getText();
	}
	public void filterByCity() {
		try {
			WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
			if(isElementPresent(cityPopupElement))
			{
				popupInputElement.sendKeys("Chennai");
				cityDropDownElement.click();
				}
			else {
				inputCityElement.click();
				inputCityElement.clear();
				inputCityElement.sendKeys("Chennai");
				cityDropDownElement.click();
		}
			wait.until(ExpectedConditions.visibilityOf(waitElement));	
	}catch(Exception e) {
		System.out.println("City filter failed:"+e.getMessage());
		}
	}
	private boolean isElementPresent(WebElement element) {
		try {
			return element.isDisplayed();
		}catch(NoSuchElementException e) {
			return false;
		}
	}
 
	public String validateUsedCarsCity() {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(20));
		wait.until(ExpectedConditions.urlContains("used-car"));
		WebElement headline=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@id='usedcarttlID']")));
		return headLineTextElement.getText().trim();
	}
	public void clickCheckBoxs() {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		for(WebElement eachElement : popularModelsCheckBoxList )
		{
			jse.executeScript("arguments[0].click();",eachElement);
		}
	}
	public void getDetailsOfCars() throws Exception {
		dataReader = new DataReader(filePath);
		dataReader.createSheet("UsedCar");
		dataReader.setCellData("UsedCar", 0, 0, "Name Of Car");
		List<WebElement> usedCarCardElements = driver.findElements(By.xpath("//div[@id='data-set-body']//div[@class='zw-sr-searchTarget col-lg-4']"));
		System.out.println(usedCarCardElements.size());
		
		int counter = 1;
		for(int i=0;i<usedCarCardElements.size();i++)
		{
			int attempts=0;
			while(attempts<2)
			{
				try {
					WebElement eachElement=driver.findElements(By.xpath("//div[@id='data-set-body']//div[@class='zw-sr-searchTarget col-lg-4']")).get(i);
					String []usedCarDetails = eachElement.getText().split("\n");
					String name=usedCarDetails.length>0?usedCarDetails[0]:"N/A";
					dataReader.setCellData("UsedCar", counter, 0, usedCarDetails[1]);
					System.out.println(name);
					counter++;
					break;
				}catch(org.openqa.selenium.StaleElementReferenceException e)
				{
					attempts++;
				}
			}
			 
		}
		dataReader.closeBook();
	}
}