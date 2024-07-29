package gift.main.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TempToken(
        String accessToken,                 //사용자 액세스 토큰 값
        String tokenType,                   //토큰타입, bearer로 고정
        String refreshToken,                //사용자 리프레시 토큰 값
        int expires_in,                     //엑세스 토큰 시간
        int refresh_token_expires_in        //리프레시 토큰 시간
) {

}
