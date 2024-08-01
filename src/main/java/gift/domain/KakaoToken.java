package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "kakaoToken")
public class KakaoToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String token;

    public KakaoToken() {}

    public KakaoToken(String userEmail, String token) {
        this.userEmail = userEmail;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getToken() {
        return token;
    }

    public KakaoToken withUpdatedToken(String token) {
        return new KakaoToken(this.userEmail, token);
    }
}