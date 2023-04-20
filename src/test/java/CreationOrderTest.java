import for_order.CreateOrder;
import for_order.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;


@RunWith(Parameterized.class)
public class CreationOrderTest {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;
    private final OrderClient order = new OrderClient();


    public CreationOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"Иван", "Петров", "Cпб", "Академическая", "89213456778", 5, "2020-06-06", "Оставьте у двери", new String[]{"BLACK"}},
                {"Тина", "Канделаки", "Казань", "Петроградская", "89213444333", 78, "2024-06-06", "Оставьте у двери", new String[]{"GREY"}},
                {"Олег", "Монгол", "Москва", "Арбат", "+79213456778", 3, "03-06", "Оставьте", new String[]{"BLACK", "GREY"}},
                {"Тимур", "Юнусов", "Москва", "Парнас", "+79213456778", 3, "03-06", "Без комментариев", null},
        };
    }


    @Test
    @DisplayName("Создание заказа")
    public void createOrderTest() {
        CreateOrder ordinaryOrder = new CreateOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        ValidatableResponse response = order.makeOrder(ordinaryOrder)
                .assertThat().body("track", notNullValue())
                .and()
                .statusCode(SC_CREATED);
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void getListOfOrdersTest() {
        ValidatableResponse response = order.gettingListOfOrder()
                .statusCode(200)
                .and()
                .assertThat().body("orders", notNullValue());
    }
}

