package gift.domain.auth;

import gift.entity.enums.SocialType;

public class JWTParameter {

    private Long id;
    private String email;
    private String socialToken = "";
    private SocialType socialType = SocialType.OTHER;
    private Integer exp = -1;

    public JWTParameter(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public JWTParameter(Long id, String email, String socialToken, SocialType socialType, Integer exp) {
        this.id = id;
        this.email = email;
        this.socialToken = socialToken;
        this.socialType = socialType;
        this.exp = exp;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSocialToken() {
        return socialToken;
    }

    public SocialType getSocialType() {
        return socialType;
    }

    public Integer getExp() {
        return exp;
    }
}
