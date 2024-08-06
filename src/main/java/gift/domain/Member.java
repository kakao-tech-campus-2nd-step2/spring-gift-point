package gift.domain;

import gift.LoginType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

import static gift.constant.ErrorMessage.EMAIL_PATTERN_ERROR_MSG;
import static gift.constant.ErrorMessage.REQUIRED_FIELD_MSG;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = EMAIL_PATTERN_ERROR_MSG)
    @NotBlank(message = REQUIRED_FIELD_MSG)
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = REQUIRED_FIELD_MSG)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LoginType loginType;

    private String kakaoAccessToken;
    private String kakaoRefreshToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes;

    public Member() {
    }

    public Member(String email, String password, LoginType loginType, String kakaoAccessToken, String kakaoRefreshToken) {
        this.email = email;
        this.password = password;
        this.loginType = loginType;
        this.kakaoAccessToken = kakaoAccessToken;
        this.kakaoRefreshToken = kakaoRefreshToken;
    }

    public Member(String email, String password, LoginType loginType) {
        this(email, password, loginType, null, null);
    }

    public Member(Long id, String email, String password, LoginType loginType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.loginType = loginType;
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

    public LoginType getLoginType() {
        return loginType;
    }

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }

    public String getKakaoRefreshToken() {
        return kakaoRefreshToken;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public void setKakaoAccessToken(String kakaoAccessToken) {
        this.kakaoAccessToken = kakaoAccessToken;
    }

    public void setKakaoRefreshToken(String kakaoRefreshToken) {
        this.kakaoRefreshToken = kakaoRefreshToken;
    }
}
