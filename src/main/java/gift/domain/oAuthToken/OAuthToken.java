package gift.domain.oAuthToken;

import gift.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class OAuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn
    private Member member;

    @Column
    private String token;

    protected OAuthToken() {

    }

    public OAuthToken(Member member, String token) {
        this.member = member;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
