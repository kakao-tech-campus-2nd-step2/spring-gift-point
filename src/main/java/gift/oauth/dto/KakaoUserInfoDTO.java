package gift.oauth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserInfoDTO {

    private long id;
    private LocalDateTime conectedAt;
    private KakaoAccountDTO kakaoAccount;

    public long getId() {
        return id;
    }

    public String getEmail() {
        return this.kakaoAccount.getEmail();
    }

    public KakaoAccountDTO getKakaoAccount() {
        return kakaoAccount;
    }
}