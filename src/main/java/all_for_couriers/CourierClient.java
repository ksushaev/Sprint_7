package all_for_couriers;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierClient {

    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private static final String PATH = "api/v1/courier";
    private static final String LOGIN_PATH = "api/v1/courier/login";

    public CourierClient() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Отправка POST-запроса на /api/v1/courier")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Отправка POST-запроса на api/v1/courier/login")
    public ValidatableResponse login(CourierLogin courierLogin) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Отправка DELETE-запроса с id курьера для удаления")
    public ValidatableResponse delete(int courierId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/v1/courier/" + courierId)
                .then();
    }
}
