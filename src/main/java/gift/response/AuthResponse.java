package gift.response;

public class AuthResponse {

    private final String token;

    public AuthResponse(String accessToken) {
        token = accessToken;
    }

    public String getAccessToken() {
        return token;
    }

}
