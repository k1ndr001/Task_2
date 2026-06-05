package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.config.ApiConfig;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class IngredientClient extends BaseClient {

    @Step("Get ingredients")
    public Response getIngredients() {
        return request()
                .when()
                .get(ApiConfig.INGREDIENTS);
    }

    @Step("Get {count} ingredient ids")
    public List<String> getIngredientIds(int count) {
        Response response = getIngredients();
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));

        List<String> ids = response.jsonPath().getList("data._id", String.class);
        assertTrue("Ingredient list should contain enough items", ids.size() >= count);
        return new ArrayList<>(ids.subList(0, count));
    }
}
