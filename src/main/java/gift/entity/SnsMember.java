package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class SnsMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long oauthId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false,unique = true)
    private String OauthAccessToken;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public SnsMember(Long oauthId, String email, String oauthAccessToken, Member member) {
        this.oauthId = oauthId;
        this.email = email;
        OauthAccessToken = oauthAccessToken;
        this.member = member;
    }

    public SnsMember() {

    }

    public Long getId() {
        return id;
    }

    public Long getOauthId() {
        return oauthId;
    }

    public String getEmail() {
        return email;
    }

    public Member getMember() {
        return member;
    }

    public String getOauthAccessToken() {
        return OauthAccessToken;
    }
}
