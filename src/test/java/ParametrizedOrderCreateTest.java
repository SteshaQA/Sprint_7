import model.OrderModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static data.TestData.*;
import static data.TestData.COMMENT;
import static data.TestData.DELIVERYDATE;
import static data.TestData.METROSTATION;
import static data.TestData.PHONE;
import static data.TestData.RENTTIME;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.hamcrest.Matchers.notNullValue;
import static steps.OrderSteps.createOrder;

@RunWith(Parameterized.class)
public class ParametrizedOrderCreateTest extends BaseAPITest {

        private final String[] color;

        public ParametrizedOrderCreateTest(String[] color) {
            this.color = color;
        }

        @Parameterized.Parameters
        public static Object[] getInputData() {
            return new Object[][]{
                    {new String[]{"BLACK"}},
                    {new String[]{"GREY"}},
                    {new String[]{"BLACK", "GREY"}},
                    {null},
            };
        }

        //заказ по верхней кнопке
        @Test
        public void OrderCreateWithColorTest() {
            OrderModel order = new OrderModel(FIRSTNAME, LASTNAME, ADDRESS, METROSTATION, PHONE, RENTTIME, DELIVERYDATE, COMMENT, color);
            createOrder(order)
                    .then()
                    .statusCode(HTTP_CREATED)
                    .body("track", notNullValue());
        }
    }
