package com.api.google.googlemaps;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.response.Response;

public class UIAutomation {
	Properties property;
	String arg1;
	Response response;
	
	List<Map<String, String>> places;
	List<Map<String, String>> browservalues;
	WebDriver driver;
	APIAutomation api;

	@Given("^open chrome and  launch google maps \"([^\"]*)\"$")
	public void open_chrome_and_launch_google_maps(String arg1) throws Throwable {

		//to check operating system	
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("mac")){
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver");
		}
		else{
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
		}
		//desired capabilities for chrome driver
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		driver = new ChromeDriver(cap.chrome());
		//launching google maps
		driver.get("http://maps.google.com/maps");

	}
	//searching destination and clicking on directions
	@Then("^verify that I entered destination as \"([^\"]*)\"$")
	public void verify_that_I_entered_destination_as(String destination) throws Throwable {
		driver.findElement(By.id("searchboxinput")).sendKeys(destination);
		
		driver.findElement(By.id("searchbox-directions")).click();
	
	}
    //providing origin and clicking on driving mode
	//assert time and distance between API call results and UI automation results
	@Then("^verify that I entered origin as \"([^\"]*)\"$")
	public void verify_that_I_entered_origin_as(String origin) throws Throwable {
		Thread.sleep(1000);
		driver.findElement(By
				.xpath("html/body/jsl/div[3]/div[8]/div[3]/div[1]/div[2]/div/div[3]/div[1]/div[1]/div[2]/div/div/input"))
				.sendKeys(origin);
		driver.findElement(
				By.xpath("html/body/jsl/div[3]/div[8]/div[3]/div[1]/div[2]/div/div[2]/div/div/div[1]/div[2]/button"))
				.click();
		api = new APIAutomation();
		Thread.sleep(1500);
		String minutes = driver
				.findElement(By
						.xpath("html/body/jsl/div[3]/div[8]/div[8]/div/div[2]/div/div/div[5]/div[1]/div[2]/div[1]/div[1]/div[1]/span[1]"))
				.getText();
		String distance = driver
				.findElement(By
						.xpath("html/body/jsl/div[3]/div[8]/div[8]/div/div[2]/div/div/div[5]/div[1]/div[2]/div[1]/div[1]/div[2]/div"))
				.getText();
		
	
		Assert.assertEquals("Distance are not the same for UI and API",distance, api.getDistance().get(origin+", USA"));
		
			Assert.assertEquals("Minutes are not the same for UI and API",minutes.concat("s"), api.getMinutes().get(origin+", USA"));
		
	
			
		
	}

}
