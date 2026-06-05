package praktikum.model;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> toMap() {
        Map<String, String> payload = new HashMap<>();
        if (email != null) {
            payload.put("email", email);
        }
        if (password != null) {
            payload.put("password", password);
        }
        if (name != null) {
            payload.put("name", name);
        }
        return payload;
    }
}
