package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoToken(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken) {
}
