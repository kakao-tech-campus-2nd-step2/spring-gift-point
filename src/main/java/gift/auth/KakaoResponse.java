package gift.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {
    public record KakaoAccount(
            @JsonProperty("profile") KakaoProfile profile,
            @JsonProperty("email") String email
    ) {
    }

    public record KakaoProfile(
            @JsonProperty("nickname") String nickname
    ) {
    }
}
