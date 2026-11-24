package api.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginRequest {
    public String login;
    public String password;
}
