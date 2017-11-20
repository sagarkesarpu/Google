package com.google.api.apichaining;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

public class APIGet {

	Properties property;
	String url;
	Response response;
	List<Map<String, String>> places;
	List<Map<String, String>> browservalues;
	private static String s = "Data";

	/* fetching the google API URL from properties file and */
	@Given("^the get apis url is \"([^\"]*)\"$")
	public void the_get_apis_url_is(String arg1) throws Throwable {
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "/config.properties");
		property = new Properties();
		property.load(fs);
		this.url = property.getProperty("url");
		response = given().when().get(arg1);
		this.url = this.url + property.getProperty("resource");
	}

	/* reading origin, destination and mode from feature file data table */
	@When("^get parameters$")
	public void get_parameters(DataTable dataTable) throws Throwable {
		this.places = new ArrayList<Map<String, String>>();
		places = dataTable.asMaps(String.class, String.class);

	}

	@Then("^the getresponse should be OK$")
	public void the_getresponse_should_be_OK() throws Throwable {
		for (int j = 0; j < places.size(); j++) {

			ResponseBody response = given().param(property.getProperty("key")).parameters(places.get(j)).when().get(url)
					.getBody();
			Thread.sleep(1000);
			// First get the JsonPath object instance from the Response
			JsonPath jsonPathEvaluator = response.jsonPath();
			Float latitude = jsonPathEvaluator.get("routes[0].legs[0].end_location.lat");
			Float longitude = jsonPathEvaluator.get("routes[0].legs[0].end_location.lng");

			// Writing the long and lat to an excel which will serve as an input
			// for the next API Call
			FileInputStream fis;
			try {
				fis = new FileInputStream(System.getProperty("user.dir") + "/Payload.xlsx");
				XSSFWorkbook wb = new XSSFWorkbook(fis);
				XSSFSheet ws = wb.getSheet(s);
				int rows = ws.getPhysicalNumberOfRows();
				int cols = ws.getRow(0).getPhysicalNumberOfCells();
				Row row = ws.createRow(rows);
				Cell cell = row.createCell(0);
				Thread.sleep(1000);
				cell.setCellValue(latitude);
				Thread.sleep(1000);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue(longitude);

				FileOutputStream output = new FileOutputStream(System.getProperty("user.dir") + "/Payload.xlsx");
				wb.write(output);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
