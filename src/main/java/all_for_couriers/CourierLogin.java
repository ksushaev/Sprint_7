package all_for_couriers;

import io.restassured.response.ValidatableResponse;

public class CourierLogin {

    private String login;
    private String password;

    public CourierLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierLogin() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static CourierLogin loginFrom(Courier courier) {
        return new CourierLogin(courier.getLogin(), courier.getPassword());
    }
}
