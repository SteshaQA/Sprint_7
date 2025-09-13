package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.CourierLoginModel;

import static io.restassured.RestAssured.given;

public class DeleteSteps {

    public static final String PATH_DELETE = "/api/v1/courier/";

    public static final String PATH_LOGIN = "/api/v1/courier/login";

    public static int getIdCourier(CourierLoginModel courierLoginModel) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierLoginModel)
                .when()
                .post(PATH_LOGIN)
                .then()
                .log().all()
                .extract().path("id");
    }

    public static Response deleteCourier(int courierId) {
        return given()
                .when()
                .delete(PATH_DELETE + courierId)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response deleteCourierWithoutId() {
//        return given()
//                .when()
//                .delete(PATH_DELETE)
//                .then()
//                .log().all()
//                .extract().response();

        return given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .pathParam("id", "null")
                .when()
                .delete("https://qa-scooter.praktikum-services.ru/api/v1/courier/{id}")
                .then()
                .log().all()
                .extract().response();
    }
}
