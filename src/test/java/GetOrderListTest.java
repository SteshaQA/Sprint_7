import model.OrderModel;
import org.junit.Test;

import static data.TestData.*;
import static data.TestData.COLOR;
import static data.TestData.COMMENT;
import static data.TestData.DELIVERYDATE;
import static data.TestData.METROSTATION;
import static data.TestData.PHONE;
import static data.TestData.RENTTIME;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.notNullValue;
import static steps.OrderListSteps.getOrderList;
import static steps.OrderSteps.createOrder;

public class GetOrderListTest extends BaseAPITest{
    @Test
    //
    public void GetOrderListTestSuccess(){

        OrderModel order = new OrderModel(FIRSTNAME, LASTNAME, ADDRESS, METROSTATION, PHONE, RENTTIME, DELIVERYDATE, COMMENT, COLOR);

        createOrder(order);
        getOrderList()
                .then()
                .statusCode(HTTP_OK)
                .body("orders", notNullValue());
    }
}
