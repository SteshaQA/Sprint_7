import model.CourierLoginModel;
import model.CourierModel;
import org.junit.After;
import org.junit.Test;
import steps.DeleteSteps;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.CourierLoginSteps.loginCourier;
import static steps.CourierSteps.createCourier;
import static steps.DeleteSteps.*;

public class CreateCourierTest extends BaseAPITest {

    private CourierModel courier;

    @Test
    //курьера можно создать
    public void createCourierTestSuccess(){

        courier = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);
        createCourier(courier)
                .then()
                .statusCode(HTTP_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    //нельзя создать двух одинаковых курьеров
    public void createTwoIdenticalCourierTest(){

        courier = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);
        createCourier(courier);
        createCourier(courier)
                .then()
                .statusCode(HTTP_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    //нельзя создать курьера без логина
    public void createCourierWithoutLoginTest(){
        courier = new CourierModel(null, PASSWORD, FIRSTNAME);
        createCourier(courier)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    //нельзя создать курьера без пароля
    public void createCourierWithoutPasswordTest(){
        courier = new CourierModel(LOGIN, null, FIRSTNAME);
        createCourier(courier)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    //можно создать курьера без имени
    public void createCourierWithoutFirstnameTest(){
        courier = new CourierModel(LOGIN, PASSWORD, null);
        createCourier(courier)
                .then()
                .statusCode(HTTP_CREATED)
                .body("ok", equalTo(true));
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
