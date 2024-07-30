package gift.user.domain;

public class UserRegisterRequest {
    private String email;
    private String password;
    private String name;
    private String profileImageUrl;
    private Long kakaoId;
    private LoginType loginType;

    // 일반 사용자 생성자
    public UserRegisterRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.loginType = LoginType.NORMAL;
    }

    // 카카오 사용자 생성자
    public UserRegisterRequest(String name, String profileImageUrl, Long kakaoId) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.kakaoId = kakaoId;
        this.loginType = LoginType.KAKAO;
    }

    // Getter 및 Setter

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Long getKakaoId() {
        return kakaoId;
    }

    public void setKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }
}

