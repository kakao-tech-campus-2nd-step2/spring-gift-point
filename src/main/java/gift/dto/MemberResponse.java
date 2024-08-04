package gift.dto;

public class MemberResponse {

    private String email;
    private String token;

    public MemberResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
