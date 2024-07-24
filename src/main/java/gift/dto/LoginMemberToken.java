package gift.dto;

public class LoginMemberToken {

    private String token;

    public LoginMemberToken() {
    }

    public LoginMemberToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
