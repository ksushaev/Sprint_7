import AllForCouriers.Courier;
import AllForCouriers.CourierClient;
import AllForCouriers.CourierLogin;
import AllForCouriers.DataForCourier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


public class CourierCreationTest {
    private CourierClient courierClient;
    private final DataForCourier dataForCourier = new DataForCourier();

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }


    @Test
    @DisplayName("Создание курьера")
    public void create2OfCourier() {
        Courier courier = dataForCourier.dataForCourier();
        ValidatableResponse response = courierClient.create(courier)
                .assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(SC_CREATED);
    }

    @Test
    @DisplayName("Создание курьера без поля password")
    public void createOfCourierWithoutPassword() {
        Courier courier = new Courier("Sasha23124356", "", "Petya");

        ValidatableResponse response = courierClient.create(courier)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание уже существующего курьера")
    public void createOfCourierWithSameLogin() {
        Courier courier = dataForCourier.dataForCourier();

        ValidatableResponse response = courierClient.create(courier);
        Courier theSameCourier = dataForCourier.dataForCourier();
        ValidatableResponse theSameResponse = courierClient.create(courier)
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);

        //удаляем курьера после создания, чтобы прошли другие тесты
        CourierLogin login = CourierLogin.loginFrom(courier);
        ValidatableResponse loginResponse = courierClient.login(login)
                .assertThat().body("id", notNullValue());
        int courierId = loginResponse.extract().path("id");
        courierClient.delete(courierId);
    }
}
