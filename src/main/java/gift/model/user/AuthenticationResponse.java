package gift.model.user;

public class AuthenticationResponse {
    private String access_token;

    public AuthenticationResponse(String accessToken) {
        this.access_token = accessToken;
    }

    // getters
    public String getAccessToken() {
        return access_token;
    }
}
