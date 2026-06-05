package praktikum.tests;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.model.User;
import praktikum.util.UserFactory;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

public class UserLoginTest {

    private final UserClient userClient = new UserClient();
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    public void existingUserCanLogIn() {
        User user = UserFactory.createUniqueUser();
        Response createResponse = userClient.createUser(user);
        accessToken = createResponse.path("accessToken");

        Response response = userClient.loginUser(user);
        accessToken = response.path("accessToken");

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    public void userCannotLogInWithIncorrectCredentials() {
        User user = UserFactory.createUniqueUser();

        userClient.loginUser(user.getEmail(), "WrongPassword123").then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
