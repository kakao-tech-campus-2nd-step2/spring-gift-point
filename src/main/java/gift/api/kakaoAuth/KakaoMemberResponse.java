package gift.api.kakaoAuth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMemberResponse(@JsonProperty("kakao_account") KakaoAccount kakaoAccount) {

    public record KakaoAccount(String email) {

    }
}
