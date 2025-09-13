package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetOrderIdStep {
    public static final String PATH_GET_ORDERID = "/api/v1/orders/track";

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
