package ForOrder;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class MethodsForOrder {

    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private static final String ORDER_PATH = "/api/v1/orders";

    public MethodsForOrder() {
        RestAssured.baseURI = BASE_URI;
    }

    public ValidatableResponse makeOrder(CreateOrder order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }
}
