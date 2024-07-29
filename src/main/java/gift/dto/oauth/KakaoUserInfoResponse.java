package gift.dto.oauth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserInfoResponse(
        long id,
        KakaoAccount kakaoAccount

) {
    public record KakaoAccount(
            String email
    ) {
    }
}
