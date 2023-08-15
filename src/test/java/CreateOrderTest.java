import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrderDataGenerator;
import order.OrderSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserDataGenerator;
import user.UserSteps;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class CreateOrderTest {
    private User user;
    private String accessToken;
    private Order order;
    @Before
    public void setUp() {
        RestAssured.baseURI = OrderSteps.baseURL;
        user = UserDataGenerator.getRandomUser();
        accessToken = UserSteps.createNewUser(user).then().extract().path("accessToken");
    }
    @Test
    @DisplayName("Создание заказа с авторизацией, с ингредиентами")
    public void createOrderWithAuthorization() {
        order = OrderDataGenerator.getOrderData();
        Response response = OrderSteps.createNewOrderWithAuthorization(accessToken, order);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", is(true));
    }
    @Test
    @DisplayName("Создание заказа без авторизации, с ингредиентами")
    public void createOrderWithoutAuthorization() {
        order = OrderDataGenerator.getOrderData();
        Response response = OrderSteps.createNewOrderNoAuthorization(order);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", is(true));
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        Response response = OrderSteps.createNewOrderNoIngredients(accessToken);
        response.then().assertThat().statusCode(400)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithWrongHash() {
        order = OrderDataGenerator.getInvalidOrderData();
        Response response = OrderSteps.createNewOrderNoAuthorization(order);
        response.then().assertThat().statusCode(500);
    }
    @After
    public void cleanUp(){
        UserSteps.deleteUser(accessToken);
    }
}
