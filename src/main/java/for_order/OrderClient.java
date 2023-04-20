package for_order;

import all_for_couriers.CourierClient;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String ORDER_PATH = "/api/v1/orders";

    public OrderClient() {
        RestAssured.baseURI = CourierClient.BASE_URI;
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

    @Step("Отправка GET-запроса на /api/v1/orders")
    public ValidatableResponse gettingListOfOrder() {
        return given()
                .get("/api/v1/orders")
                .then();
    }
}