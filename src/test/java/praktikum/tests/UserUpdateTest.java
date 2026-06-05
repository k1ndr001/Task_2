package praktikum.tests;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.client.UserClient;
import praktikum.model.User;
import praktikum.util.UserFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class UserUpdateTest {

    private final UserClient userClient = new UserClient();
    private final String fieldName;
    private String accessToken;

    public UserUpdateTest(String fieldName) {
        this.fieldName = fieldName;
    }

    @Parameterized.Parameters(name = "field={0}")
    public static Collection<Object[]> fields() {
        return Arrays.asList(new Object[][]{
                {"email"},
                {"password"},
                {"name"}
        });
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    public void authorizedUserCanUpdateField() {
        User user = UserFactory.createUniqueUser();
        Response createResponse = userClient.createUser(user);
        accessToken = createResponse.path("accessToken");
        Map<String, String> payload = UserFactory.createUpdatePayload(fieldName);

        Response response = userClient.updateUser(accessToken, payload);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));

        if ("email".equals(fieldName)) {
            String updatedEmail = payload.get(fieldName);
            user.setEmail(updatedEmail);
            response.then().body("user.email", equalTo(updatedEmail));
        } else if ("name".equals(fieldName)) {
            String updatedName = payload.get(fieldName);
            user.setName(updatedName);
            response.then().body("user.name", equalTo(updatedName));
        } else {
            String updatedPassword = payload.get(fieldName);
            user.setPassword(updatedPassword);
            Response loginResponse = userClient.loginUser(user);
            accessToken = loginResponse.path("accessToken");
            loginResponse.then()
                    .statusCode(200)
                    .body("success", equalTo(true));
        }
    }

    @Test
    public void unauthorizedUserCannotUpdateField() {
        Map<String, String> payload = UserFactory.createUpdatePayload(fieldName);

        userClient.updateUserWithoutAuthorization(payload).then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
