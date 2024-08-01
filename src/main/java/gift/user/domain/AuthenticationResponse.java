package gift.user.domain;

public class AuthenticationResponse {

    private final String token;
    private final String email;

    public AuthenticationResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }
}
