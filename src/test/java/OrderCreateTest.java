import io.restassured.response.Response;
import model.OrderModel;
import org.junit.After;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static steps.CancelOrder.cancelOrderWithTrack;
import static steps.OrderSteps.createOrder;

public class OrderCreateTest extends BaseAPITest {

    private OrderModel order;
    private int trackId;

    @Test
    //тело ответа содержит track
    public void orderCreateTestSuccess(){

        order = new OrderModel(FIRSTNAME, LASTNAME, ADDRESS, METROSTATION, PHONE, RENTTIME, DELIVERYDATE, COMMENT, COLOR);
        trackId = createOrder(order)
                .then()
                .statusCode(HTTP_CREATED)
                .body("track", notNullValue())
                .extract().path("track");;
    }

    @After
    //попытки отменить созданный заказ, ответ сервера 404 BAD_REQUEST - баг API
    public void cancelOrderAfterCreateSuccess() {
            try {
                Response response = cancelOrderWithTrack(trackId);
                int statusCode = response.getStatusCode();

                if (statusCode == HTTP_OK) {
                    response.then()
                            .body("ok", equalTo(true));
                    System.out.println("Заказ успешно отменен");

                } else if (statusCode == HTTP_BAD_REQUEST) {
                    response.then()
                            .body("message", equalTo("Недостаточно данных для поиска"));
                    System.out.println("Заказ не может быть отменен");

                }
            }
            catch (Exception e) {
                System.out.println("Заказ не был отменен");
            }
        }
}
