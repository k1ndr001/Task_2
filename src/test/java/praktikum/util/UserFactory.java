package praktikum.util;

import io.qameta.allure.Step;
import praktikum.model.User;

import java.util.Map;
import java.util.UUID;

public final class UserFactory {

    private UserFactory() {
    }

    @Step("Generate unique user")
    public static User createUniqueUser() {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        return new User("test_" + suffix + "@example.com", "Password123", "User" + suffix);
    }

    @Step("Generate user body without {fieldName}")
    public static Map<String, String> createUserPayloadWithout(String fieldName) {
        Map<String, String> payload = createUniqueUser().toMap();
        payload.remove(fieldName);
        return payload;
    }

    @Step("Generate update body for {fieldName}")
    public static Map<String, String> createUpdatePayload(String fieldName) {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        switch (fieldName) {
            case "email":
                return Map.of(fieldName, "updated_" + suffix + "@example.com");
            case "password":
                return Map.of(fieldName, "UpdatedPassword123");
            case "name":
                return Map.of(fieldName, "UpdatedUser" + suffix);
            default:
                throw new IllegalArgumentException("Unknown user field: " + fieldName);
        }
    }
}
