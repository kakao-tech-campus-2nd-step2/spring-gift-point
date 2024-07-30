package gift.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfo(
    @JsonProperty("kakao_account")
    KakaoAccount kakaoAccount
) {
    @Override
    public KakaoAccount kakaoAccount() {
        System.out.println(this.kakaoAccount);
        return kakaoAccount;
    }
}
