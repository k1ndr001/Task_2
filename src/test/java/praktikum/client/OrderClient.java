package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.config.ApiConfig;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OrderClient extends BaseClient {

    @Step("Create authorized order")
    public Response createOrder(String accessToken, List<String> ingredientIds) {
        return request()
                .header("Authorization", accessToken)
                .body(ingredientsPayload(ingredientIds))
                .when()
                .post(ApiConfig.ORDERS);
    }

    @Step("Create order without authorization")
    public Response createOrderWithoutAuthorization(List<String> ingredientIds) {
        return request()
                .body(ingredientsPayload(ingredientIds))
                .when()
                .post(ApiConfig.ORDERS);
    }

    @Step("Get authorized user orders")
    public Response getUserOrders(String accessToken) {
        return request()
                .header("Authorization", accessToken)
                .when()
                .get(ApiConfig.ORDERS);
    }

    @Step("Get user orders without authorization")
    public Response getUserOrdersWithoutAuthorization() {
        return request()
                .when()
                .get(ApiConfig.ORDERS);
    }

    private Map<String, List<String>> ingredientsPayload(List<String> ingredientIds) {
        return Collections.singletonMap("ingredients", ingredientIds);
    }
}
