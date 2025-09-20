package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderListSteps {
    public static final String PATH_GETLISTORDER = "/api/v1/orders";

    @Step("Send GET request to /api/v1/orders - get order list")
    public static Response getOrderList(){
        return given()
                .get(PATH_GETLISTORDER)
                .then()
                .log().all()
                .extract().response();
    }
}
