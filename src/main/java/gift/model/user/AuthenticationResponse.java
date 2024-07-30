package gift.model.user;

public class AuthenticationResponse {
    private String accessToken;

    public AuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    // getters
    public String getAccessToken() {
        return accessToken;
    }
}
