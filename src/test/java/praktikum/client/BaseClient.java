package praktikum.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import praktikum.config.ApiConfig;

public class BaseClient {

    protected RequestSpecification request() {
        return RestAssured.given()
                .filter(new AllureRestAssured())
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }
}
