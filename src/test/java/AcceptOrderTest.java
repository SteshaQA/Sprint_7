import model.CourierLoginModel;
import model.CourierModel;
import model.OrderModel;
import org.junit.After;
import org.junit.Test;
import steps.DeleteSteps;

import static data.TestData.*;
import static data.TestData.COLOR;
import static data.TestData.COMMENT;
import static data.TestData.DELIVERYDATE;
import static data.TestData.METROSTATION;
import static data.TestData.PHONE;
import static data.TestData.RENTTIME;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.AcceptOrderStep.*;
import static steps.CancelOrder.getTrackOrder;
import static steps.CourierLoginSteps.loginCourier;
import static steps.CourierSteps.createCourier;
import static steps.DeleteSteps.deleteCourier;
import static steps.DeleteSteps.getIdCourier;

public class AcceptOrderTest extends BaseAPITest {

    private CourierModel createCourierData;
    private CourierLoginModel loginData;
    private OrderModel order;
    private int courierId;
    private int trackId;
    private int orderId;

    @Test
    //успешный запрос возвращает ok: true;
    public void AcceptOrderTestSuccess() {
        createCourierData = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);

        String login = createCourierData.getLogin();
        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(login, password);

        order = new OrderModel(FIRSTNAME, LASTNAME, ADDRESS, METROSTATION, PHONE, RENTTIME, DELIVERYDATE, COMMENT, COLOR);
        createCourier(createCourierData);
        trackId = getTrackOrder(order);
        courierId = getIdCourier(loginData);
        orderId = getOrderIdWithTrack(trackId);
        acceptOrderWithTrack(orderId, courierId)
                .then()
                .statusCode(HTTP_OK)
                .body("ok", equalTo(true));
    }

    @Test
    //если не передать id заказа, запрос вернёт ошибку;
    public void AcceptOrderTestWithoutId() {
        createCourierData = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);

        String login = createCourierData.getLogin();
        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(login, password);

        order = new OrderModel(FIRSTNAME, LASTNAME, ADDRESS, METROSTATION, PHONE, RENTTIME, DELIVERYDATE, COMMENT, COLOR);
        createCourier(createCourierData);
        trackId = getTrackOrder(order);
        courierId = getIdCourier(loginData);
        orderId = getOrderIdWithTrack(trackId);
        acceptOrderWithoutOrderId(courierId)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    //если не передать id курьера, запрос вернёт ошибку;
    public void AcceptOrderTestWithoutCourierId() {
        createCourierData = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);

        String login = createCourierData.getLogin();
        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(login, password);

        order = new OrderModel(FIRSTNAME, LASTNAME, ADDRESS, METROSTATION, PHONE, RENTTIME, DELIVERYDATE, COMMENT, COLOR);
        createCourier(createCourierData);
        trackId = getTrackOrder(order);
        courierId = getIdCourier(loginData);
        orderId = getOrderIdWithTrack(trackId);
        acceptOrderWithoutCourierId(orderId)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    //если передать неверный номер заказа, запрос вернёт ошибку
    public void AcceptOrderTestWithIncorrectOrderId() {
        createCourierData = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);

        String login = createCourierData.getLogin();
        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(login, password);

        order = new OrderModel(FIRSTNAME, LASTNAME, ADDRESS, METROSTATION, PHONE, RENTTIME, DELIVERYDATE, COMMENT, COLOR);
        createCourier(createCourierData);
        trackId = getTrackOrder(order);
        courierId = getIdCourier(loginData);
        orderId = getOrderIdWithTrack(trackId);
        acceptOrderWithTrack(orderId + 1111111, courierId)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Заказа с таким id не существует"));
    }
    @Test
    //если передать неверный id курьера, запрос вернёт ошибку
    public void AcceptOrderTestWithIncorrectCourierId() {
        createCourierData = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);

        String login = createCourierData.getLogin();
        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(login, password);

        order = new OrderModel(FIRSTNAME, LASTNAME, ADDRESS, METROSTATION, PHONE, RENTTIME, DELIVERYDATE, COMMENT, COLOR);
        createCourier(createCourierData);
        trackId = getTrackOrder(order);
        courierId = getIdCourier(loginData);
        orderId = getOrderIdWithTrack(trackId);
        acceptOrderWithTrack(orderId, courierId + 1111111)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    @After
    public void deleteCourierAfterCreateSuccess() {
        if (createCourierData != null) {
            try {
                String login = createCourierData.getLogin();
                String password = createCourierData.getPassword();

                loginData = new CourierLoginModel(login, password);
                loginCourier(loginData);
                int courierID = DeleteSteps.getIdCourier(loginData);
                deleteCourier(courierID)
                        .then()
                        .statusCode(HTTP_OK)
                        .body("ok", equalTo(true));
            } catch (Exception e) {
                System.out.println("Курьер не был создан/не получилось залогиниться");
            }
        }
    }

}