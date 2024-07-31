package gift.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class KakaoOauthMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long kakaoIdentifier;

    @Column
    private String accessToken;

    @OneToOne
    @JoinColumn(unique = true)
    private Member member;

    protected KakaoOauthMember() {
    }

    public KakaoOauthMember(Long kakaoIdentifier, String accessToken, Member member) {
        this.kakaoIdentifier = kakaoIdentifier;
        this.accessToken = accessToken;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Long getKakaoIdentifier() {
        return kakaoIdentifier;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Member getMember() {
        return member;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "KakaoOauthMember{" +
            "id=" + id +
            ", kakaoIdentifier=" + kakaoIdentifier +
            ", member=" + member +
            '}';
    }
}
