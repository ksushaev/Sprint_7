import all_for_couriers.Courier;
import all_for_couriers.CourierClient;
import all_for_couriers.CourierLogin;
import all_for_couriers.DataForCourier;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class CourierLoginTest {

    private CourierClient courierClient;
    private final DataForCourier dataForCourier = new DataForCourier();
    private int courierId;

    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void courierAutorizationTest() {
        Courier courier = dataForCourier.dataForCourier();
        ValidatableResponse response = courierClient.createCourier(courier);
        CourierLogin login = CourierLogin.loginFrom(courier);
        ValidatableResponse loginResponse = courierClient.login(login)
                .assertThat().body("id", notNullValue());
        courierId = loginResponse.extract().path("id");

        assertEquals(HttpStatus.SC_OK, loginResponse.extract().statusCode());
    }

    @Test
    @DisplayName("Несуществующий курьер")
    public void notRealCourierAutorizationTest() {
        CourierLogin courierLogin = new CourierLogin("1juigtigwniugt", "1234");
        ValidatableResponse loginResponse = courierClient.login(courierLogin)
                .assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Курьер без пароля")
    public void courierWithoutOneFieldTest() {
        Courier courier = dataForCourier.dataForCourier();
        ValidatableResponse response = courierClient.createCourier(courier);
        CourierLogin login = CourierLogin.loginFrom(courier);
        login.setPassword("");
        ValidatableResponse loginResponse = courierClient.login(login)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }
    @After
    public void deleteCourier() {
        courierClient.delete(courierId);
    }
}
