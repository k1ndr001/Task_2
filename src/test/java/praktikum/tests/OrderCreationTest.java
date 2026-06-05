package praktikum.tests;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import praktikum.client.IngredientClient;
import praktikum.client.OrderClient;
import praktikum.client.UserClient;
import praktikum.model.User;
import praktikum.util.UserFactory;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreationTest {

    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();
    private final IngredientClient ingredientClient = new IngredientClient();
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    public void authorizedUserCanCreateOrderWithIngredients() {
        User user = UserFactory.createUniqueUser();
        Response createResponse = userClient.createUser(user);
        accessToken = createResponse.path("accessToken");
        List<String> ingredientIds = ingredientClient.getIngredientIds(2);

        orderClient.createOrder(accessToken, ingredientIds).then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.owner.email", equalTo(user.getEmail()))
                .body("order.number", notNullValue())
                .body("order.ingredients.size()", equalTo(ingredientIds.size()));
    }

    @Test
    public void unauthorizedUserCanCreateOrderWithIngredients() {
        List<String> ingredientIds = ingredientClient.getIngredientIds(2);

        orderClient.createOrderWithoutAuthorization(ingredientIds).then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    public void orderCannotBeCreatedWithoutIngredients() {
        orderClient.createOrderWithoutAuthorization(Collections.emptyList()).then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void orderCannotBeCreatedWithIncorrectIngredientHash() {
        orderClient.createOrderWithoutAuthorization(Collections.singletonList("incorrect_hash")).then()
                .statusCode(500)
                .body(containsString("Internal Server Error"));
    }
}
