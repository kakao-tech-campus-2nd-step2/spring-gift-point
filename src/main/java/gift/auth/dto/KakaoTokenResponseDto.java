package gift.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.member.domain.Email;
import gift.member.domain.Member;

public record KakaoTokenResponseDto(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("expires_in") Integer expiresIn,
        @JsonProperty("scope") String scope) {
}
