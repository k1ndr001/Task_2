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

public class UserCreationTest {

    private final UserClient userClient = new UserClient();
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    public void uniqueUserCanBeCreated() {
        User user = UserFactory.createUniqueUser();

        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", notNullValue());
    }

    @Test
    public void existingUserCannotBeCreatedAgain() {
        User user = UserFactory.createUniqueUser();
        Response firstResponse = userClient.createUser(user);
        accessToken = firstResponse.path("accessToken");

        firstResponse.then()
                .statusCode(200)
                .body("success", equalTo(true));

        userClient.createUser(user).then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void userCannotBeCreatedWithoutEmail() {
        userClient.createUser(UserFactory.createUserPayloadWithout("email")).then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void userCannotBeCreatedWithoutPassword() {
        userClient.createUser(UserFactory.createUserPayloadWithout("password")).then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void userCannotBeCreatedWithoutName() {
        userClient.createUser(UserFactory.createUserPayloadWithout("name")).then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
