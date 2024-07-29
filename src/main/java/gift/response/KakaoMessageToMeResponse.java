package gift.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMessageToMeResponse(
    @JsonProperty(value = "result_code")
    Integer resultCode
) {

}
