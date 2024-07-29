package gift.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class KakaoToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(nullable = false, unique = true)
    private final Long memberId;

    @Column(nullable = false)
    private final String accessToken;

    @Column(nullable = false)
    private final String refreshToken;

    protected KakaoToken() {
        this(null, null, null, null);
    }

    public KakaoToken(Long memberId, String accessToken, String refreshToken) {
        this(null, memberId, accessToken, refreshToken);
    }

    public KakaoToken(Long id, Long memberId, String accessToken, String refreshToken) {
        this.id = id;
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
