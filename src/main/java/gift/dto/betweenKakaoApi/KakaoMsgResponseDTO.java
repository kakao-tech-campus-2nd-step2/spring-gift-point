package gift.dto.betweenKakaoApi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMsgResponseDTO(@JsonProperty("result_code") Integer resultCode){ }