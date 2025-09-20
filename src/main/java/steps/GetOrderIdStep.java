package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetOrderIdStep {
    public static final String PATH_GET_ORDERID = "/api/v1/orders/track";

    @Step("Send GET request to /api/v1/orders/track - get order id with trackId")
    public static Response getOrderId(int trackId){
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .queryParam("t", trackId)
                .when()
                .get(PATH_GET_ORDERID)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Send GET request to /api/v1/orders/track - get order id without trackId")
    public static Response getOrderIdWithoutTrackId(){
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_GET_ORDERID)
                .then()
                .log().all()
                .extract().response();
    }
}
