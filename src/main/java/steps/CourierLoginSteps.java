package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.CourierLoginModel;

import static io.restassured.RestAssured.given;

public class CourierLoginSteps {

    public static final String PATH_LOGIN = "/api/v1/courier/login";

    public static Response loginCourier(CourierLoginModel courierLoginModel) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courierLoginModel)
                .when()
                .post(PATH_LOGIN)
                .then()
                .log().all()
                .extract().response();
        }
    }
