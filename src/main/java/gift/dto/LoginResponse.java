package gift.dto;

public class LoginResponse {
    private final String access_token;

    public LoginResponse(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }
}
