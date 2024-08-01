package gift.web.dto.request;

import gift.web.validation.constraints.Password;
import jakarta.validation.constraints.Email;

public class LoginRequest {

    @Email
    private final String email;

    @Password
    private final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}