package gift.entity;

import gift.domain.KakaoToken;
import gift.util.LoginType;
import jakarta.persistence.*;


@Entity
@Table(name = "members")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LoginType loginType;

    private String kakaoToken;

    public MemberEntity() {

    }

    public MemberEntity(String email, String password, LoginType loginType) {
        this.email = email;
        this.password = password;
        this.loginType = loginType;
    }

    public MemberEntity(String email, String password, LoginType loginType, String kakaoToken) {
        this.email = email;
        this.password = password;
        this.loginType = loginType;
        this.kakaoToken = kakaoToken;
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

    public String getKakaoToken() {
        return kakaoToken;
    }

}
