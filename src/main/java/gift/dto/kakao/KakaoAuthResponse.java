package gift.dto.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoAuthResponse(
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {
}

