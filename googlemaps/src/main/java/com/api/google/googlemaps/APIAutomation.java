package com.api.google.googlemaps;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

public class APIAutomation {

	Properties property;
	String url;
	Response response;
	List<Map<String, String>> places;
	List<Map<String, String>> browservalues;

	static Map<String, String> distance = new HashMap<String, String>();
	static Map<String, String> minutes = new HashMap<String, String>();
    
	/*reading properties files and getting the google api url and resource*/
	@Given("^the apis are up and running for \"([^\"]*)\"$")
	public void the_apis_are_up_and_running_for(String arg1) throws Throwable {
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "/config.properties");
		property = new Properties();
		property.load(fs);

		this.url = property.getProperty("url");
		response = given().when().get(arg1);
		this.url = this.url + property.getProperty("resource");
	}
	/*reading origin, destination and mode from feature file data table*/
	@When("^flow parameters$")
	public void flow_parameters(DataTable dataTable) throws Throwable {
		this.places = new ArrayList<Map<String, String>>();
		places = dataTable.asMaps(String.class, String.class);

	}

	/*getting distance and minutes from response body and adding it to a map*/
	@Then("^the status should be OK$")
	public void the_status_should_be_OK() throws Throwable {
		for (int j = 0; j < places.size(); j++) {

			ResponseBody response = given().param(property.getProperty("key")).parameters(places.get(j)).when().get(url)
					.getBody();
			// First get the JsonPath object instance from the Response
			JsonPath jsonPathEvaluator = response.jsonPath();
			String address = jsonPathEvaluator.get("routes[0].legs[0].start_address");
			String distance1 = jsonPathEvaluator.get("routes[0].legs[0].distance.text");
			String minutes1 = jsonPathEvaluator.get("routes[0].legs[0].duration.text");

			distance.put(address, distance1.concat("les"));
			minutes.put(address, minutes1);

		}
	}

	public Map<String, String> getDistance() {
		return distance;
	}

	public Map<String, String> getMinutes() {
		return minutes;
	}

}
