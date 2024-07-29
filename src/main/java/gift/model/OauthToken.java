package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "oauth_token")
public class OauthToken extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    @NotNull
    @Column(name = "access_token")
    private String accessToken;
    @NotNull
    @Column(name = "access_token_expires_in")
    private Integer accessTokenExpiresIn;
    @NotNull
    @Column(name = "refresh_token")
    private String refreshToken;
    @NotNull
    @Column(name = "refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "oauth_type")
    private OauthType oauthType;

    protected OauthToken() {
    }

    public OauthToken(Member member, OauthType oauthType, String accessToken, Integer accessTokenExpiresIn, String refreshToken, Integer refreshTokenExpiresIn) {
        this.member = member;
        this.oauthType = oauthType;
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
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

    public void updateToken(String accessToken, Integer accessTokenExpiresIn, String refreshToken, Integer refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        if (refreshToken != null) {
            this.refreshToken = refreshToken;
            this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        }
    }

    public Boolean canUseAccessToken() {
        if (super.getCreatedDate().plusSeconds(accessTokenExpiresIn).isAfter(LocalDateTime.now())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean canUseRefreshToken() {
        if (super.getCreatedDate().plusSeconds(refreshTokenExpiresIn).isAfter(LocalDateTime.now())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
