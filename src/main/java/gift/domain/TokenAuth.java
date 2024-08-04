package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "token_auth")
public class TokenAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accessToken;

    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public TokenAuth(String accessToken, Member member) {
        this(accessToken, null, member);
    }

    public TokenAuth() { }

    public TokenAuth(String accessToken, String refreshToken, Member member) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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

    @PreRemove
    private void preRemove() {
        if (member != null) {
            member.setTokenAuth(null);
        }
    }

}
