package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "kakao_token")
public class KakaoToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;

    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "member_id", unique = true)
    private Member member;

    public KakaoToken() {

    }

    public KakaoToken(Member member, String accessToken, String refreshToken) {
        this.member = member;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Member getMember() {
        return member;
    }
}
