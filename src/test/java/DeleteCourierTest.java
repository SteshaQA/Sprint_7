import model.CourierLoginModel;
import model.CourierModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.DeleteSteps;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.CourierLoginSteps.loginCourier;
import static steps.CourierSteps.createCourier;
import static steps.DeleteSteps.deleteCourier;
import static steps.DeleteSteps.deleteCourierWithoutId;

public class DeleteCourierTest extends BaseAPITest {

    private CourierModel courier;
    private CourierLoginModel loginData;

    @Before
    public void courierCreateBefore(){
        courier = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);
        createCourier(courier);
    }

    @Test
    //успешный запрос возвращает ok: true
    public void deleteCourierTestSuccess() {
        String login = courier.getLogin();
        String password = courier.getPassword();

        loginData = new CourierLoginModel(login, password);
        loginCourier(loginData);
        int courierID = DeleteSteps.getIdCourier(loginData);
        deleteCourier(courierID)
                .then()
                .statusCode(HTTP_OK)
                .body("ok", equalTo(true));
    }

    @Test
    //если отправить запрос без id, вернётся ошибка
    public void deleteCourierTestWithoutId() {

        deleteCourierWithoutId()
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    //если отправить запрос с несуществующим id, вернётся ошибка
    public void deleteCourierTestNotExistId() {
        String login = courier.getLogin();
        String password = courier.getPassword();

        loginData = new CourierLoginModel(login, password);
        loginCourier(loginData);
        deleteCourier(333)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Курьера с таким id нет."));
    }

    @After
    public void deleteCourierAfterCreateSuccess() {
        if (courier != null && courier.getLogin() != null && courier.getPassword() != null) {
            try {
                String login = courier.getLogin();
                String password = courier.getPassword();

                CourierLoginModel loginData = new CourierLoginModel(login, password);
                loginCourier(loginData);
                int courierID = DeleteSteps.getIdCourier(loginData);
                deleteCourier(courierID)
                        .then()
                        .statusCode(HTTP_OK)
                        .body("ok", equalTo(true));
            }
            catch (Exception e) {
                System.out.println("Курьер не был создан");
            }
        }
    }
}
