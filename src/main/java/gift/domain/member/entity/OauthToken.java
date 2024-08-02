package gift.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    uniqueConstraints = { @UniqueConstraint(columnNames = { "member_id", "provider" }) }
)
public class OauthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    protected OauthToken() {
    }

    public OauthToken(Long id, Member member, AuthProvider provider, String accessToken, String refreshToken) {
        this.id = id;
        this.member = member;
        this.provider = provider;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void updateInfo(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        if (refreshToken != null) {
            this.refreshToken = refreshToken;
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
