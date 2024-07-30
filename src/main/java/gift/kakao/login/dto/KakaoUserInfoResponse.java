package gift.kakao.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserInfoResponse {
    @JsonProperty("id")
    private String id;
    @JsonProperty("connected_at")
    private String connectedAt;

    public KakaoUserInfoResponse() {
    }

    public KakaoUserInfoResponse(String id, String connectedAt) {
        this.id = id;
        this.connectedAt = connectedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(String connectedAt) {
        this.connectedAt = connectedAt;
    }
}
