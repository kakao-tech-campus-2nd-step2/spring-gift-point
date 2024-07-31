package gift.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record KakaoUserInfoResponse(
        Long id,

        @JsonProperty("connected_at")
        LocalDateTime connectedAt,

        @JsonProperty("kakao_account")
        KakaoAcountResponse kakaoAccount
) {
}
