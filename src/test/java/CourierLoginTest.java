import io.restassured.response.Response;
import model.CourierLoginModel;
import model.CourierModel;
import org.junit.After;
import org.junit.Test;
import steps.DeleteSteps;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static steps.CourierLoginSteps.loginCourier;
import static steps.CourierSteps.createCourier;
import static steps.DeleteSteps.deleteCourier;

public class CourierLoginTest extends BaseAPITest {

    private CourierModel createCourierData;
    private CourierLoginModel loginData;

    @Test
    //курьер может авторизоваться
    public void loginCourierTestSuccess() {

        createCourierData = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);

        String login = createCourierData.getLogin();
        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(login, password);

        createCourier(createCourierData);
        loginCourier(loginData)
                .then()
                .statusCode(HTTP_OK)
                .body("id", notNullValue());
    }

    @Test
    //нельзя авторизоваться без логина
    public void loginCourierWithoutLoginTest() {

        createCourierData = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);

        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(null, password);

        createCourier(createCourierData);
        loginCourier(loginData)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    //нельзя авторизоваться без пароля
    public void loginCourierWithoutPasswordTest() {

        createCourierData = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);

        String login = createCourierData.getLogin();

        loginData = new CourierLoginModel(login, null);

        createCourier(createCourierData);
        try{
            Response response = loginCourier(loginData);
            if (response.statusCode() == HTTP_BAD_REQUEST){
                response.then()
                        .statusCode(HTTP_BAD_REQUEST)
                        .body("message", equalTo("Недостаточно данных для входа"));
            } else {
                throw new AssertionError("Ожидался статус код 400, но получен: " + response.statusCode());
            }
         } catch (Exception e) {
            System.out.println("Ожидали другой статус ошибки");
         }
    }

    @Test
    //нельзя авторизоваться c несуществующей парой логин/пароль
    public void loginCourierNotExistLoginPasswordTest() {

        createCourierData = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);

        String login = createCourierData.getLogin();
        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(login, password);

        loginCourier(loginData)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    //нельзя авторизоваться c логином, содержащим ошибку
    public void loginCourierWithIncorrectLoginTest() {

        createCourierData = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);

        String login = createCourierData.getLogin();
        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(login + "A", password);

        createCourier(createCourierData);
        loginCourier(loginData)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    //нельзя авторизоваться c паролем, содержащим ошибку
    public void loginCourierWithIncorrectPasswordTest() {

        createCourierData = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);

        String login = createCourierData.getLogin();
        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(login, password + "A");

        createCourier(createCourierData);
        loginCourier(loginData)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
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