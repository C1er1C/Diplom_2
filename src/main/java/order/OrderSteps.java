package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import user.User;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    public static String baseURL = "https://stellarburgers.nomoreparties.site/";
    public static String orderPath = "/api/orders";
    @Step("Создание заказа с авторизацией")
    public static Response createNewOrderWithAuthorization(String accessToken, Order order) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(orderPath);
    }
    @Step("Создание заказа без авторизации")
    public static Response createNewOrderNoAuthorization(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(orderPath);
    }
    @Step("Создание заказа без ингридиентов")
    public static Response createNewOrderNoIngredients(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .post(orderPath);
    }
    @Step("Получить заказы конкретного пользователя")
    public static Response getSomeOrders(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get(orderPath);
    }
}
