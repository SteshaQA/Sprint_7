package steps;

import model.CourierModel;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;


public class CourierSteps {

    public static final String PATH_CREATE = "/api/v1/courier";

    public static Response createCourier(CourierModel courierModel){
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courierModel)
                .when()
                .post(PATH_CREATE)
                .then()
                .log().all()
                .extract().response();
    }
}
