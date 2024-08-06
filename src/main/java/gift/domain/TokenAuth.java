package gift.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "token_auth")
public class TokenAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accessToken;

    private String refreshToken;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date accessTokenExpiry;

    @Temporal(TemporalType.TIMESTAMP)
    private Date refreshTokenExpiry;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public TokenAuth(String accessToken, Member member) {
        this(accessToken, null, null, null, member);
    }

    public TokenAuth() { }

    public TokenAuth(String accessToken, String refreshToken, Date accessTokenExpiry, Date refreshTokenExpiry, Member member) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenExpiry = refreshTokenExpiry;
        setMember(member);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.setTokenAuth(null);
        }

        this.member = member;
        if (member != null && member.getTokenAuth() != this) {
            member.setTokenAuth(this);
        }
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getMemberId(){
        return member.getId();
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Date getAccessTokenExpiry() {
        return accessTokenExpiry;
    }

    public void setAccessTokenExpiry(Date accessTokenExpiry) {
        this.accessTokenExpiry = accessTokenExpiry;
    }

    public Date getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }

    public void setRefreshTokenExpiry(Date refreshTokenExpiry) {
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    @PreRemove
    private void preRemove() {
        if (member != null) {
            member.setTokenAuth(null);
        }
    }

}
