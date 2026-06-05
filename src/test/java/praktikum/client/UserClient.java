package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.config.ApiConfig;
import praktikum.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserClient extends BaseClient {

    @Step("Create user")
    public Response createUser(User user) {
        return createUser(user.toMap());
    }

    @Step("Create user with request body")
    public Response createUser(Map<String, String> payload) {
        return request()
                .body(payload)
                .when()
                .post(ApiConfig.REGISTER);
    }

    @Step("Log in user")
    public Response loginUser(User user) {
        return loginUser(user.getEmail(), user.getPassword());
    }

    @Step("Log in user with email {email}")
    public Response loginUser(String email, String password) {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("password", password);

        return request()
                .body(payload)
                .when()
                .post(ApiConfig.LOGIN);
    }

    @Step("Update authorized user")
    public Response updateUser(String accessToken, Map<String, String> payload) {
        return request()
                .header("Authorization", accessToken)
                .body(payload)
                .when()
                .patch(ApiConfig.USER);
    }

    @Step("Update user without authorization")
    public Response updateUserWithoutAuthorization(Map<String, String> payload) {
        return request()
                .body(payload)
                .when()
                .patch(ApiConfig.USER);
    }

    @Step("Delete user")
    public Response deleteUser(String accessToken) {
        return request()
                .header("Authorization", accessToken)
                .when()
                .delete(ApiConfig.USER);
    }
}
