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

public class UserOrderTest {
    private User user;
    private String accessToken;
    private Order order;
    @Before
    public void setUp() {
        RestAssured.baseURI = OrderSteps.baseURL;
        user = UserDataGenerator.getRandomUser();
        accessToken = UserSteps.createNewUser(user).then().extract().path("accessToken");
        order = OrderDataGenerator.getOrderData();
        OrderSteps.createNewOrderNoAuthorization(order);
    }
    @Test
    @DisplayName("Получение заказов конкретного авторизованного пользователя")
    public void getUserOrderWithAuthorization() {
        Response response = OrderSteps.getSomeOrders(accessToken);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", is(true));
    }
    @Test
    @DisplayName("Получение заказов конкретного неавторизованного пользователя")
    public void getUserOrderWithoutAuthorization() {
        Response response = OrderSteps.getSomeOrders("");
        response.then().assertThat().statusCode(401)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
    @After
    public void cleanUp(){
        UserSteps.deleteUser(accessToken);
    }
}
