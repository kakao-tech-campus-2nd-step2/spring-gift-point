package gift.web.client.dto;

import gift.domain.Member;
import gift.domain.constants.Platform;
import gift.domain.vo.Email;

public class KakaoAccount {

    private KakaoProfile profile;
    private String email;
    private Boolean isEmailValid;
    private Boolean isEmailVerified;

    public KakaoAccount(KakaoProfile profile, String email, Boolean isEmailValid,
        Boolean isEmailVerified) {
        this.profile = profile;
        this.email = email;
        this.isEmailValid = isEmailValid;
        this.isEmailVerified = isEmailVerified;
    }

    public KakaoProfile getProfile() {
        return profile;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getEmailValid() {
        return isEmailValid;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public Member toMember() {
        return new Member.Builder()
            .name(profile.getNickname())
            .email(Email.from(email))
            .platform(Platform.KAKAO)
            .build();
    }
}
