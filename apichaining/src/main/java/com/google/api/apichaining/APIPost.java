package com.google.api.apichaining;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class APIPost {

	Properties property;
	String url;
	Response response;
	List<Map<String, String>> postdata;
	Map<String, Double> body;
	static List<String> placeid = new ArrayList<String>();

	public static List<String> getPlaceid() {
		return placeid;
	}

	private static String s = "Data";

	/*
	 * Retrieving the URL and resource from Properties file
	 */
	@Given("^the post apis url is \"([^\"]*)\"$")
	public void the_post_apis_url_is(String arg1) throws Throwable {
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "/config.properties");
		property = new Properties();
		property.load(fs);

		this.url = property.getProperty("url");
		response = given().when().get(arg1);
		this.url = this.url + property.getProperty("postresource");
	}

	/* Reading the request payload data from feature file */
	@When("^post parameters$")
	public void post_parameters(DataTable datatable) throws Throwable {
		this.postdata = new ArrayList<Map<String, String>>();
		postdata = datatable.asMaps(String.class, String.class);

	}

	/* Constructing the POST API call with the request payload */
	@Then("^the postresponse should be OK$")
	public void the_postresponse_should_be_OK() throws Throwable {

		FileInputStream fis;

		// reading a part of the request payload from the excel file and
		// converting them to a format which is accurate for the google add a
		// place api call to work accurately
		try {
			fis = new FileInputStream(System.getProperty("user.dir") + "/Payload.xlsx");
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet ws = wb.getSheet(s);
			int rowCount = ws.getLastRowNum() - ws.getFirstRowNum();
			for (int i = 1; i < rowCount + 1; i++) {
				Row row = ws.getRow(i);

				Double latitude = row.getCell(0).getNumericCellValue();
				Double longitude = row.getCell(1).getNumericCellValue();
				this.body = new HashMap<String, Double>();

				this.body.put("lat", latitude);
				this.body.put("lng", longitude);
				Gson gson = new Gson();
				String json = gson.toJson(body);

				String type = gson.toJson(postdata).replace("{", "").replace("}", "").replace("\\", "");
				StringBuilder sb = new StringBuilder(type);

				sb.deleteCharAt(0);
				sb.deleteCharAt(sb.length() - 1);
				sb.deleteCharAt(sb.length() - 1);
				sb.deleteCharAt(sb.indexOf("types") + 7);

				Response rs = given().queryParam("key",property.getProperty("key"))
						.body("{" + "\"location\":" + json + "," + sb.toString() + "}").when().post(this.url).then()
						.and().extract().response();
				JsonPath jsonPathEvaluator = rs.jsonPath();
				String latitude1 = jsonPathEvaluator.get("place_id");
				placeid.add(latitude1);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
