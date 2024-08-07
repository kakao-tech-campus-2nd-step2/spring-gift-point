package gift.message.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoMessageForMeResponseDto(
    Integer resultCode
) {

    public boolean isSuccess() {
        if (resultCode != null) {
            return resultCode == 0;
        }
        return false;
    }
}
