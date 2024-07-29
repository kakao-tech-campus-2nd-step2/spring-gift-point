package gift.Model;

public class MemberAccessToken {
    private final String accessToken;
    public MemberAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
