package gift.kakaoApi.dto.token;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoTokenResponse(

    String tokenType,

    String accessToken,

    String expiresIn,

    String refreshToken,

    String refreshTokenExpiresIn
) {

}
