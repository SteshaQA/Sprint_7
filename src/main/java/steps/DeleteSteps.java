package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.CourierLoginModel;

import static io.restassured.RestAssured.given;

public class DeleteSteps {

    public static final String PATH_DELETE = "/api/v1/courier/";

    public static final String PATH_LOGIN = "/api/v1/courier/login";

    @Step("Send POST request to /api/v1/courier/login - get courier id")
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

    @Step("Send DELETE request to /api/v1/courier/ -  delete courier with courierId")
    public static Response deleteCourier(int courierId) {
        return given()
                .when()
                .delete(PATH_DELETE + courierId)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Send DELETE request to /api/v1/courier/ -  delete courier without courierId")
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
