package gift.users.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoTokenDTO(String tokenType, String accessToken, int expiresIn,
                            String refreshToken, int refreshTokenExpiresIn) {

}
