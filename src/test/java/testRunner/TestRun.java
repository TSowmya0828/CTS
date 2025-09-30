package testRunner;


import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
//		tags= "@regression",
		
		features = {".//FeatureFiles/"},
		glue = "stepDefinations",
		plugin = { "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"pretty","html:target/htmlreport.html"}
		)

public class TestRun{

}



// TestNG
/*
import org.junit.runner.RunWith;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


//@RunWith(Cucumber.class)
@CucumberOptions(
//		tags= "@regression",
		
		features = {".//FeatureFiles/"},
		glue = "stepDefinations",
		plugin = { "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"pretty","html:target/htmlreport.html"}
		)

public class TestRun extends AbstractTestNGCucumberTests{

}*/