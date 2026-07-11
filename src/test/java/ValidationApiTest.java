import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

public class ValidationApiTest {
    String baseUrl = "https://api.rizqifauzan.com/";
    String token;
    JSONObject requestBody = new JSONObject();
    String nama = "Bayu Yudha Pratama";
    String email = "bayuyudha" + System.currentTimeMillis() + "@gmail.com";
    String password = "Bayu123321";

    @Test
    public void testRegister() {
        requestBody.put("nama", nama);
        requestBody.put("email", email);
        requestBody.put("password", password);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                    .post(baseUrl + "api/auth/register");

        response.then()
                .assertThat()
                .statusCode(201);
        System.out.println("Email anda " + email + " Berhasil di regist!");
    }
    @Test (dependsOnMethods = "testRegister")
    public void testLogin() {
        requestBody.put("email", email);
        requestBody.put("password", password);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post(baseUrl + "api/auth/login");

        response.then()
                .assertThat()
                .statusCode(200);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());

        // Extract token from response
        token = response.jsonPath().getString("data.token");
        System.out.println("Token: " + token);
    }
    @Test (dependsOnMethods = "testLogin")
    public void testGetListUsers() {
        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                    .get("https://api.rizqifauzan.com/api/auth/me")
                .then()
                    .log().all()
                .assertThat()
                    .statusCode(200);
    }

    @Test (dependsOnMethods = "testLogin")
    public void testLogout() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                    .post("https://api.rizqifauzan.com/api/auth/logout")
                .then()
                    .log().all();
    }

    @Test
    public void testWrongLogin() {
        requestBody.put("email", email);
        requestBody.put("password", "testsalah");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post(baseUrl + "api/auth/login");

        response.then()
                .log().all()
                .statusCode(401);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
    }
    @Test
    public void testFailedLogin() {
        requestBody.put("email", email);
        requestBody.put("password", password);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post(baseUrl + "api/auth/login");

        response.then()
                .log().all()
                .body("data.email", Matchers.equalTo("wrongemail@example.com"))
                .body("data.password", Matchers.equalTo("wrongpass123321"))
                .statusCode(200);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
    }

    @Test
    public void testBlankLogin() {
        requestBody.put("email", "");
        requestBody.put("password", "");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post(baseUrl + "api/auth/login");

        response.then()
                .log().all()
                .statusCode(400);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
    }

}
