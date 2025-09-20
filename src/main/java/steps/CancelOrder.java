package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.OrderModel;

import static io.restassured.RestAssured.given;

public class CancelOrder {
    public static final String PATH_CANCEL = "/api/v1/orders/cancel";

    public static final String PATH_ORDER = "/api/v1/orders";

    @Step("Send POST request to /api/v1/orders - get order track")
    public static int getTrackOrder(OrderModel orderModel) {
        return given()
                .contentType(ContentType.JSON)
                .body(orderModel)
                .when()
                .post(PATH_ORDER)
                .then()
                .log().all()
                .extract().path("track");
    }

    @Step("Send PUT request to /api/v1/orders/cancel - cancel order with track")
    public static Response cancelOrderWithTrack(int trackId){
        String requestBody = String.format("{\"track\": %d}", trackId);
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(PATH_CANCEL)
                .then()
                .log().all()
                .extract().response();
    }
}
