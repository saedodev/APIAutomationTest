import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;
import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class StudentManagement extends ValidationApiTest {

    String studentId;
    String namaStudent = "Tamaki Ridho";
    String nis = "998875";
    String kelas = "X-RPL-1";
    String jurusan = "RPL";
    String emailStudent = "ridhotest" + System.currentTimeMillis() + "@gmail.com";
    String rawTimestamp = String.valueOf(System.currentTimeMillis());
    String telepon = "08" + rawTimestamp.substring(rawTimestamp.length() - 10);
    String alamat = "Kalimantan";
    String created_by = "User Bayu automation-test";

    File jsonSchema = new File("src/test/resources/jsonschema/GetUserIdStudent.json");

    @Test(dependsOnMethods = "testLogin")
    public void createStudent() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("nama", namaStudent);
        requestBody.put("nis", nis);
        requestBody.put("kelas", kelas);
        requestBody.put("jurusan", jurusan);
        requestBody.put("email", emailStudent);
        requestBody.put("telepon", telepon);
        requestBody.put("alamat", alamat);
        requestBody.put("created_by", created_by);

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post(baseUrl + "api/siswa");

        response.then()
                .log().all()
                .statusCode(201)
                .body("data.nama", equalTo(namaStudent))
                .body("data.nis", equalTo(nis))
                .body("data.created_at", notNullValue())
                .body("data.updated_at", notNullValue());

        studentId = response.jsonPath().getString("data.id");
        System.out.println("Nama student " + namaStudent + " berhasil di-create!");
    }

    @Test(dependsOnMethods = "testLogin")
    public void getAllStudent() {
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "api/siswa");

        response.then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
    }

    @Test(dependsOnMethods = "createStudent")
    public void getByIdStudent() {
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "api/siswa/" + studentId);

        response.then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("data.id", equalTo(studentId))
                .body("data.nama", equalTo(namaStudent));
    }

    @Test(dependsOnMethods = "createStudent")
    public void testUpdateStudent() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("nama", "Iroha Ridho");
        requestBody.put("kelas", "XI-TKJ-2");
        requestBody.put("alamat", "Banjarmasin, Kalimantan Selatan");


        Response response = RestAssured.given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .pathParam("id", studentId)
                .body(requestBody.toString())
                .put("/api/siswa/{id}");

        response.then()
                .log().all()
                .assertThat()
                .statusCode(200);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
    }

    @Test(dependsOnMethods = "testUpdateStudent")
    public void testPatchStudent() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("nama", "Kaname Ridho");

        Response response = RestAssured.given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .pathParam("id", studentId)
                .body(requestBody.toString())
                .patch("/api/siswa/{id}");

        response.then()
                .log().all()
                .assertThat()
                .statusCode(200);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
    }

    @Test(dependsOnMethods = "testPatchStudent")
    public void testDestroyStudent() {
        Response response = RestAssured.given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", studentId)
                .delete("/api/siswa/{id}");

        response.then()
                .log().all()
                .assertThat()
                .statusCode(200);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
    }
}