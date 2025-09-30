package pageObject;
 
import java.time.Duration;
import java.util.Set;
 
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
 
import factory.BaseClass;
 
public class LoginWithGoogle {
	WebDriver driver;
	public LoginWithGoogle(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
 
	@FindBy (xpath = "//div[@id='h-m-icn']//parent::div//following-sibling::a")
	WebElement logoElement;
	
	@FindBy (xpath = "//div[@id='forum_login_div_lg']")
	WebElement loginBtnElement;
	
	@FindBy (xpath = "//h4[@class='hd-ctr mb-15 zs']//span[1]")
	WebElement headerTextElement;
	
	@FindBy (xpath = "//div[@class='lgn-sc c-p txt-l pl-30 pr-30 googleSignIn']")
	WebElement goolgleLoginElement;
	
	@FindBy(id="identifierId")
	WebElement inputEmailElement;

	@FindBy(xpath="//div[contains(text(),'Couldn’t sign you in')]")
	WebElement errorTxtElement;
	
	@FindBy(xpath="//span[normalize-space()='Next']")
	WebElement nextButtonElement;
	
		
	public void clickOnLogo() {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(15));
		wait.until(ExpectedConditions.elementToBeClickable(logoElement));
		JavascriptExecutor js=(JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);",logoElement);
		js.executeScript("arguments[0].click();",logoElement);		
	}
	
	public void clickOnLogInButton() {
		loginBtnElement.click();
	}
	
	public String navigateToLoginWindow() {
		return headerTextElement.getText();
	}
	
	public void loginByGoogle() {
		goolgleLoginElement.click();
	}
	
	public void handelWindows() {
		Set<String> windowsId = driver.getWindowHandles();
		for(String id : windowsId) {
			driver.switchTo().window(id);
		}
	}
	
	public String validateGoogleAccountLoginWindow() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleContains("Sign in - Google Accounts"));
		String  txt = driver.getTitle();
		System.out.println(txt);
		return txt;
	}
	
	public void enterInvalidEmail() {
		
		inputEmailElement.clear();
		inputEmailElement.sendKeys(BaseClass.randomeString()+"@gmail.com");
		//inputEmailElement.sendKeys(Keys.TAB);
		nextButtonElement.click();
		
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(errorTxtElement));
	}
	
	public String getErrorText() {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(errorTxtElement));
		String txt = errorTxtElement.getText();
		System.out.println("Error Message:" +txt);
		return txt;
	}
	
	
}