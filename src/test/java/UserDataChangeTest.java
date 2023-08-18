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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserDataChangeTest {
    private User user;
    private String accessToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = UserSteps.baseURL;
        user = UserDataGenerator.getRandomUser();
        accessToken = UserSteps.createNewUser(user).then().extract().path("accessToken");
    }
    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void updateUserDataWithAuthorization() {
        UserLogin login = new UserLogin(user.getEmail(), user.getPassword());
        Response response = UserSteps.updateUserData(accessToken, login);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", is(true));
    }
    @Test
    @DisplayName("Изменение данных пользователя без авторизацией")
    public void updateUserDataWithoutAuthorization() {
        UserLogin login = new UserLogin(null, null);
        Response response = UserSteps.updateUserData("", login);
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
