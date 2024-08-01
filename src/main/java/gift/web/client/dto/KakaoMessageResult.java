package gift.web.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class KakaoMessageResult {

    private final Integer resultCode;

    @JsonCreator
    public KakaoMessageResult(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public Integer getResultCode() {
        return resultCode;
    }
}
