package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.OrderModel;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    public static final String PATH_ORDER = "/api/v1/orders";

    @Step("Send POST request to /api/v1/orders - create order")
    public static Response createOrder(OrderModel orderModel){
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(orderModel)
                .when()
                .post(PATH_ORDER)
                .then()
                .log().all()
                .extract().response();
    }
}
