package tests;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;

import io.restassured.filter.cookie.CookieFilter;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class Login {
	@Test
	public void validAuthentication() {
		Map<String, Object> authPayload = new HashMap<>();
		authPayload.put("method", "password");
		authPayload.put("factors[user]", "thevernon124+ctreacy@gmail.com");
		authPayload.put("factors[password]", "Test1234!");
		given()
			.contentType(ContentType.URLENC.withCharset("UTF-8"))
			.formParams(authPayload)
		.when()
			.post("https://trello.com/1/authentication")
		.then()
			.assertThat()
				.statusCode(200)
				.body("$", hasKey("code"));
	}
	@Test
	public void validLogin() {
		String dsc = given().get("https://trello.com").getCookie("dsc");
		Map<String, Object> authPayload = new HashMap<>();
		authPayload.put("method", "password");
		authPayload.put("factors[user]", "thevernon124+ctreacy@gmail.com");
		authPayload.put("factors[password]", "Test1234!");
		String authCode = given()
				.cookie("dsc", dsc)
				.contentType(ContentType.URLENC.withCharset("UTF-8"))
				.formParams(authPayload)
				.post("https://trello.com/1/authentication")
				.then()
				.extract()
				.response()
				.path("code");
		Map<String, Object> loginPayload = new HashMap<>();
		System.out.println("LOGIN PAYLOAD: " + loginPayload);
		given()
			.cookie("dsc", dsc)
			.contentType(ContentType.URLENC.withCharset("UTF-8"))
			.formParams(loginPayload)
		.when()
			.post("https://trello.com/1/authorization/session")
		.then()
			.assertThat()
			.statusCode(204);
	}
}
