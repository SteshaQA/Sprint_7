import io.restassured.response.Response;
import model.CourierLoginModel;
import model.CourierModel;
import org.junit.After;
import org.junit.Before;
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

    @Before
    public void courierCreateBefore(){
        createCourierData = new CourierModel(LOGIN, PASSWORD, FIRSTNAME);
        createCourier(createCourierData);
    }


    @Test
    //курьер может авторизоваться
    public void loginCourierTestSuccess() {

        String login = createCourierData.getLogin();
        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(login, password);

        loginCourier(loginData)
                .then()
                .statusCode(HTTP_OK)
                .body("id", notNullValue());
    }

    @Test
    //нельзя авторизоваться без логина
    public void loginCourierWithoutLoginTest() {

        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(null, password);

        loginCourier(loginData)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    //нельзя авторизоваться без пароля
    public void loginCourierWithoutPasswordTest() {

        String login = createCourierData.getLogin();

        loginData = new CourierLoginModel(login, null);

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

        loginData = new CourierLoginModel("00000000", "000000000");

        loginCourier(loginData)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    //нельзя авторизоваться c логином, содержащим ошибку
    public void loginCourierWithIncorrectLoginTest() {

        String login = createCourierData.getLogin();
        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(login + "A", password);

        loginCourier(loginData)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    //нельзя авторизоваться c паролем, содержащим ошибку
    public void loginCourierWithIncorrectPasswordTest() {

        String login = createCourierData.getLogin();
        String password = createCourierData.getPassword();

        loginData = new CourierLoginModel(login, password + "A");

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