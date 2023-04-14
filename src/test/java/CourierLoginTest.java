import AllForCouriers.Courier;
import AllForCouriers.CourierClient;
import AllForCouriers.CourierLogin;
import AllForCouriers.DataForCourier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class CourierLoginTest {

    private CourierClient courierClient;

    private final DataForCourier dataForCourier = new DataForCourier();

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void courierAutorizationTest() {
        Courier courier = dataForCourier.dataForCourier();
        ValidatableResponse response = courierClient.create(courier);
        CourierLogin login = CourierLogin.loginFrom(courier);
        ValidatableResponse loginResponse = courierClient.login(login)
                .assertThat().body("id", notNullValue());
        assertEquals(HttpStatus.SC_OK, loginResponse.extract().statusCode());

        //удаляем курьера после создания
        int courierId = loginResponse.extract().path("id");
        courierClient.delete(courierId);
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
        ValidatableResponse response = courierClient.create(courier);
        CourierLogin login = CourierLogin.loginFrom(courier);
        login.setPassword("");
        ValidatableResponse loginResponse = courierClient.login(login)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }
}
