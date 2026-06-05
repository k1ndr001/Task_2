package praktikum.config;

public final class ApiConfig {

    public static final String BASE_URL = System.getProperty(
            "base.url",
            "https://qa-stellarburgers.education-services.ru"
    );

    public static final String REGISTER = "/api/auth/register";
    public static final String LOGIN = "/api/auth/login";
    public static final String USER = "/api/auth/user";
    public static final String INGREDIENTS = "/api/ingredients";
    public static final String ORDERS = "/api/orders";

    private ApiConfig() {
    }
}
