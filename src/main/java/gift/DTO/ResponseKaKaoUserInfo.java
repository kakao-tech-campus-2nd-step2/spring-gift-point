package gift.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResponseKaKaoUserInfo {
    KakaoAccount kakaoAccount;

    public ResponseKaKaoUserInfo() {
    }

    public KakaoAccount getKakaoAccount() {
        return kakaoAccount;
    }
}
