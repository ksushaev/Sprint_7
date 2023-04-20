package all_for_couriers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class DataForCourier {

    public Courier dataForCourier() {
        return new Courier("ksussfdehцaev24", "1234", "Ksusha");
    }
}
