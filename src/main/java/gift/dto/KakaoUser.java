package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUser(
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {
    public record KakaoAccount(
            String email,
            @JsonProperty("profile")
            Profile profile) {
    }

    public record Profile(
            @JsonProperty("nickname")
            String nickName
    ) {
    }
}
