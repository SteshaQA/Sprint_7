package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AcceptOrderStep {

    public static final String PATH_ACCEPT_ORDER = "/api/v1/orders/accept/{orderId}";
    public static final String PATH_GET_ORDERID = "/api/v1/orders/track";

    @Step("Send PUT request to /api/v1/orders/accept/ with orderId")
    public static Response acceptOrderWithTrack(int orderId, int courierID){
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .pathParam("orderId", orderId)
                .queryParam("courierId", courierID)
                .when()
                .put(PATH_ACCEPT_ORDER)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Send PUT request to /api/v1/orders/accept/ without orderId")
    public static Response acceptOrderWithoutOrderId(int courierID){
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .queryParam("courierId", courierID)
                .when()
                .put("/api/v1/orders/accept/")
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Send PUT request to /api/v1/orders/accept/ without courierID")
    public static Response acceptOrderWithoutCourierId(int orderId){
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .pathParam("orderId", orderId)
                .when()
                .put(PATH_ACCEPT_ORDER)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Send GET request to /api/v1/orders/track with trackId")
    public static int getOrderIdWithTrack(int trackId){
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .queryParam("t", trackId)
                .when()
                .get(PATH_GET_ORDERID)
                .then()
                .log().all()
                .extract().path("order.id");
    }

}
