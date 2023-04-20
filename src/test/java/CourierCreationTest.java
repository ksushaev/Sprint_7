import all_for_couriers.Courier;
import all_for_couriers.CourierClient;
import all_for_couriers.CourierLogin;
import all_for_couriers.DataForCourier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;


public class CourierCreationTest {
    private CourierClient courierClient;
    private final DataForCourier dataForCourier = new DataForCourier();
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courier = new Courier();
        courierClient = new CourierClient();
    }


    @Test
    @DisplayName("Создание курьера")
    public void create2OfCourier() {
        Courier courier = dataForCourier.dataForCourier();
        ValidatableResponse response = courierClient.createCourier(courier)
                .assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(SC_CREATED);

        CourierLogin login = CourierLogin.loginFrom(courier);
        ValidatableResponse loginResponse = courierClient.login(login);
        courierId = loginResponse.extract().path("id");
    }


    @Test
    @DisplayName("Создание курьера без поля password")
    public void createOfCourierWithoutPassword() {
        Courier courier = new Courier("Sasha23124356", "", "Petya");

        ValidatableResponse response = courierClient.createCourier(courier)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание уже существующего курьера")
    public void createOfCourierWithSameLogin() {
        Courier courier = dataForCourier.dataForCourier();

        ValidatableResponse response = courierClient.createCourier(courier);
        Courier theSameCourier = dataForCourier.dataForCourier();
        ValidatableResponse theSameResponse = courierClient.createCourier(courier)
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);

        CourierLogin login = CourierLogin.loginFrom(courier);
        ValidatableResponse loginResponse = courierClient.login(login);
        courierId = loginResponse.extract().path("id");
    }

    @After
    public void deleteCourier() {
        courierClient.delete(courierId);
    }
}
