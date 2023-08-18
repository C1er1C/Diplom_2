import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserDataGenerator;
import user.UserLogin;
import user.UserSteps;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class LoginUserTest {
    private User user;
    private String accessToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = UserSteps.baseURL;
        user = UserDataGenerator.getRandomUser();
        accessToken = UserSteps.createNewUser(user).then().extract().path("accessToken");
    }
    @Test
    @DisplayName("Авторизация под существующим пользователем")
    public void loginUserSuccess() {
        UserLogin login = new UserLogin(user.getEmail(), user.getPassword() );
        Response response = UserSteps.loginUser(login);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", is(true));
    }
    @Test
    @DisplayName("Авторизация с неверным логином")
    public void loginUserWrongLogin() {
        UserLogin login = new UserLogin(null, user.getPassword() );
        Response response = UserSteps.loginUser(login);
        response.then().assertThat().statusCode(401)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void loginUserWrongPassword() {
        UserLogin login = new UserLogin(user.getEmail(), null );
        Response response = UserSteps.loginUser(login);
        response.then().assertThat().statusCode(401)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
    @After
    public void cleanUp(){
            UserSteps.deleteUser(accessToken);
    }
}
