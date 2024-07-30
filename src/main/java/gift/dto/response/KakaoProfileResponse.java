package gift.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoProfileResponse(
        KakaoAccount kakaoAccount
) {
    public record KakaoAccount(
            Profile profile
    ) { // 권한 이슈로 이메일 대신 nickname으로 대체
        public record Profile(
                String nickname
        ) {}
    }
}