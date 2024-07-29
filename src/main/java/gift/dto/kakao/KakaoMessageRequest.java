package gift.dto.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoMessageRequest(
        String objectType,
        String text,
        String webUrl,
        String mobileWebUrl
) {
}
