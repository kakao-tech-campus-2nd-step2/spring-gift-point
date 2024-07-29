package gift.domain.member;

import jakarta.persistence.*;

@Entity
public class SocialAccount {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Column(nullable = false, unique = true)
    private Long socialId;

    private String accessToken;
    private String refreshToken;

    protected SocialAccount() {
    }

    public SocialAccount(SocialType socialType, Long socialId, String accessToken, String refreshToken) {
        this.socialType = socialType;
        this.socialId = socialId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public SocialAccount(SocialType socialType, String refreshToken) {
        this.socialType = socialType;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public SocialType getSocialType() {
        return socialType;
    }

    public Long getSocialId() {
        return socialId;
    }

    public void changeAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
