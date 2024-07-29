package gift.domain;

import jakarta.persistence.*;

@Entity
public class KakaoToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false, unique = true)
    private Long memberId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "access_token", nullable = false)
    private String accessToken;


    public KakaoToken() {}

    public KakaoToken(Long id, Long memberId, String accessToken) {
        this.id = id;
        this.memberId = memberId;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
