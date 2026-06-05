package praktikum.tests;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import praktikum.client.IngredientClient;
import praktikum.client.OrderClient;
import praktikum.client.UserClient;
import praktikum.model.User;
import praktikum.util.UserFactory;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

public class UserOrdersTest {

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
    public void authorizedUserCanGetOrders() {
        User user = UserFactory.createUniqueUser();
        Response createResponse = userClient.createUser(user);
        accessToken = createResponse.path("accessToken");
        List<String> ingredientIds = ingredientClient.getIngredientIds(2);
        int orderNumber = orderClient.createOrder(accessToken, ingredientIds)
                .then()
                .statusCode(200)
                .extract()
                .path("order.number");

        orderClient.getUserOrders(accessToken).then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders.number", hasItem(orderNumber))
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }

    @Test
    public void unauthorizedUserCannotGetOrders() {
        orderClient.getUserOrdersWithoutAuthorization().then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
