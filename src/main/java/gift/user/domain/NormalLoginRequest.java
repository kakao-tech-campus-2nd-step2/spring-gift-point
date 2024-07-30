package gift.user.domain;

public class NormalLoginRequest implements UserLoginRequest {

    private String email;
    private String password;

    public NormalLoginRequest(String email, String password) {
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
