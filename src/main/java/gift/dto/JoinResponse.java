package gift.dto;

public class JoinResponse {
    private final String access_token;

    public JoinResponse(String access_token) {
        this.access_token = access_token;
    }
    public String getAccess_token() {
        return access_token;
    }
}
