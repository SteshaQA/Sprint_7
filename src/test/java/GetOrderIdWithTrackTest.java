import model.OrderModel;
import org.junit.Test;

import static data.TestData.*;
import static data.TestData.COLOR;
import static data.TestData.COMMENT;
import static data.TestData.DELIVERYDATE;
import static data.TestData.METROSTATION;
import static data.TestData.PHONE;
import static data.TestData.RENTTIME;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static steps.CancelOrder.getTrackOrder;
import static steps.GetOrderIdStep.getOrderId;
import static steps.GetOrderIdStep.getOrderIdWithoutTrackId;

public class GetOrderIdWithTrackTest extends BaseAPITest{

    private OrderModel order;
    private int trackId;
    private int orderId;

    @Test
    //успешный запрос возвращает объект с заказом
    public void orderCreateTestSuccess() {

        order = new OrderModel(FIRSTNAME, LASTNAME, ADDRESS, METROSTATION, PHONE, RENTTIME, DELIVERYDATE, COMMENT, COLOR);
        trackId = getTrackOrder(order);
        getOrderId(trackId)
                .then()
                .statusCode(HTTP_OK)
                .body("order", notNullValue());
    }

    @Test
    //запрос без номера заказа возвращает ошибку;
    public void orderCreateTestWithoutTrackId() {

        order = new OrderModel(FIRSTNAME, LASTNAME, ADDRESS, METROSTATION, PHONE, RENTTIME, DELIVERYDATE, COMMENT, COLOR);
        getOrderIdWithoutTrackId()
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    //запрос без номера заказа возвращает ошибку;
    public void orderCreateTestWithIncorrectTrackId() {

        order = new OrderModel(FIRSTNAME, LASTNAME, ADDRESS, METROSTATION, PHONE, RENTTIME, DELIVERYDATE, COMMENT, COLOR);
        trackId = getTrackOrder(order);
        getOrderId(trackId+1111111)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Заказ не найден"));
    }
}