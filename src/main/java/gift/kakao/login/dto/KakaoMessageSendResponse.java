package gift.kakao.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoMessageSendResponse {
    @JsonProperty("result_code")
    private Integer resultCode;

    public KakaoMessageSendResponse() {
    }

    public KakaoMessageSendResponse(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }
}
