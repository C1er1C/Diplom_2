import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserDataGenerator;
import user.UserSteps;
import io.restassured.response.Response;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CreateUserTest {
    private User user;
    private String accessToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = UserSteps.baseURL;
        user = UserDataGenerator.getRandomUser();
    }
    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUserSuccess(){
        Response response = UserSteps.createNewUser(user);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", is(true));
        accessToken = response.then().extract().path("accessToken");
    }
    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createUserDuplicate(){
        accessToken = UserSteps.createNewUser(user).then().extract().path("accessToken");
        Response response = UserSteps.createNewUser(user);
        response.then().assertThat().statusCode(403)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("User already exists"));
    }
    @Test
    @DisplayName("Создание пользователя, не заполнив одно из обязательных полей")
    public void createUserNoEmail() {
        user.setEmail(null);
        Response response = UserSteps.createNewUser(user);
        response.then().assertThat().statusCode(403)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
    @After
    public void cleanUp(){
        if (user.getEmail() != null) {
            UserSteps.deleteUser(accessToken);
        }
    }
}
