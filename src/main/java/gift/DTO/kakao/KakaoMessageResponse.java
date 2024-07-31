package gift.DTO.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMessageResponse(
    @JsonProperty("result_code") int resultCode
) {
}
