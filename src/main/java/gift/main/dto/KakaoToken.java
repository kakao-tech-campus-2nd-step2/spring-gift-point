package gift.main.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoToken(
        String accessToken,                 //사용자 액세스 토큰 값
        String tokenType,                   //토큰타입, bearer로 고정
        String refreshToken,                //사용자 리프레시 토큰 값
        int expires_in,                     //엑세스 토큰 시간 (초)
        int refresh_token_expires_in,        //리프레시 토큰 시간
        LocalDateTime expirationDate         //직접 계산한 만료시간
) {
    public KakaoToken(TempToken token, LocalDateTime createDateTime) {
        this(token.accessToken(), token.tokenType(), token.refreshToken(), token.expires_in(), token.refresh_token_expires_in(), createDateTime.plusSeconds(token.expires_in()));
    }
}
