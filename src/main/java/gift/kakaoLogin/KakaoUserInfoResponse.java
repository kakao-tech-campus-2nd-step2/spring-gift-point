package gift.kakaoLogin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserInfoResponse {
    Long id;
    @JsonProperty("connected_at")
    String connectedAt;

    public KakaoUserInfoResponse(Long id, String connectedAt) {
        this.id = id;
        this.connectedAt = connectedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConnectedAtt() {
        return connectedAt;
    }

    public void setConnected_at(String connectedAt) {
        this.connectedAt = connectedAt;
    }
}
