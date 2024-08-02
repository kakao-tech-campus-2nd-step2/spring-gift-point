package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "token_auth")
public class TokenAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public TokenAuth(String token, Member member) {
        this.token = token;
        setMember(member);
    }

    public TokenAuth() { }

    public String getToken() {
        return token;
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

    public void setToken(String token) {
        this.token = token;
    }

    public Long getMemberId(){
        return member.getId();
    }

}
