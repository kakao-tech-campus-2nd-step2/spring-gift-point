package gift.member.persistence.entity;

import gift.global.domain.OAuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;
    private String accessToken;
    private String refreshToken;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public Member(String email, OAuthProvider oAuthProvider, String accessToken, String refreshToken) {
        this.email = email;
        this.oAuthProvider = oAuthProvider;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    protected Member() {}

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public OAuthProvider getOAuthProvider() {
        return oAuthProvider;
    }

    public void updateToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
