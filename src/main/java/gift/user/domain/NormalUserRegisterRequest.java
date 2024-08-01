package gift.user.domain;

public class NormalUserRegisterRequest implements UserRegisterRequest {
    private String email;
    private String password;
    private String name;

    public NormalUserRegisterRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public LoginType getLoginType() {
        return LoginType.NORMAL;
    }
}
