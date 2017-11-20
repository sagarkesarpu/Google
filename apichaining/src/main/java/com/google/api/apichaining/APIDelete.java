package com.google.api.apichaining;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;

import com.google.gson.Gson;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class APIDelete {
	Properties property;
	String url;
	Response response;
	private static String s = "Data";

	/*
	 * Retrieving the URL and resource from Properties file
	 */
	@Given("^delete the place_id from the api add a place response\"([^\"]*)\"$")
	public void delete_the_place_id_from_the_api_add_a_place_response(String arg1) throws Throwable {
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "/config.properties");
		property = new Properties();
		property.load(fs);
		this.url = property.getProperty("url");
		this.url = this.url + property.getProperty("deleteresource");

	}

	/*
	 * Retrieving the place id from Post API and constructing POST API Call for
	 * deleting the place
	 */

	@When("^post parameters place_id$")
	public void post_parameters_place_id() throws Throwable {
		APIPost apipost = new APIPost();
		for (int i = 0; i < apipost.getPlaceid().size(); i++) {
			String place_id = apipost.getPlaceid().get(i);
			Gson gson = new Gson();
			String json = gson.toJson("place_id\"" + ":" + "\"" + place_id);
			StringBuilder sb = new StringBuilder(json);
			sb.deleteCharAt(9);
			sb.deleteCharAt(11);

			Response responses = given().queryParam("key",property.getProperty("key"))
					.body("{" + sb.toString() + "}").when().post(this.url).then().and().extract().response();
			JsonPath jsonPathEvaluator = responses.jsonPath();
			String latitude1 = jsonPathEvaluator.get("status");
			Assert.assertEquals("Status is not ok", latitude1, "OK");
		}

	}

	@Then("^the response status should be OK$")
	public void the_response_status_should_be_OK() throws Throwable {
		
		//deleting whatever data was written into the excel from GET API Call
		FileInputStream fis;
		fis = new FileInputStream(System.getProperty("user.dir") + "/Payload.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet ws = wb.getSheet(s);

		for (int i = 1; i < ws.getLastRowNum() + 1; i++) {
			XSSFRow row = ws.getRow(i);
			ws.removeRow(row);
		}
		FileOutputStream output = new FileOutputStream(System.getProperty("user.dir") + "/Payload.xlsx");
		wb.write(output);
	}

}
