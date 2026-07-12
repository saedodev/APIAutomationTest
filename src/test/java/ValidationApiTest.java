import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

public class ValidationApiTest {

    protected String baseUrl = "https://api.rizqifauzan.com/";
    protected static String token;

    protected String nama = "Bayu Yudha Pratama";
    protected String email = "bayuyudha" + System.currentTimeMillis() + "@gmail.com";
    protected String password = "Bayu123321";


    @Test(groups = {"auth"})
    public void testRegister() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("nama", nama);
        requestBody.put("email", email);
        requestBody.put("password", password);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post(baseUrl + "api/auth/register");

        response.then()
                .log().all()
                .assertThat()
                .statusCode(201);

        System.out.println("Email anda " + email + " berhasil diregistrasi!");
    }


    @Test(groups = {"auth"}, dependsOnMethods = "testRegister")
    public void testLogin() {
        JSONObject requestBody = new JSONObject();
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

        token = response.jsonPath().getString("data.token");
        System.out.println("Token: " + token);
    }

    @Test(groups = {"validation"}, dependsOnMethods = "testLogin")
    public void testGetListUsers() {
        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "api/auth/me")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }


    @Test(groups = {"validation"}, dependsOnMethods = "testLogin")
    public void testLogout() {
        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post(baseUrl + "api/auth/logout")
                .then()
                .log().all();
    }


    @Test(groups = {"validation"})
    public void testWrongLogin() {
        JSONObject requestBody = new JSONObject();
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

    @Test(groups = {"validation"})
    public void testFailedLogin() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post(baseUrl + "api/auth/login");

        response.then()
                .log().all()
                .assertThat()
                .statusCode(400);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
    }

    @Test(groups = {"validation"})
    public void testBlankLogin() {
        JSONObject requestBody = new JSONObject();
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