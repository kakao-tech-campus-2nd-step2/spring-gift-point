package gift.domain;

public class KakaoInfo {
    private final Long id;
    private final String email;
    private final String password;

    public KakaoInfo(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
