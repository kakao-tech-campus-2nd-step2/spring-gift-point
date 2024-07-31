package gift.doamin.user.dto;

import java.util.Map;

public class KakaoOAuthUserInfoResponse {

    private Long id;
    private Map<String, String> properties;

    public KakaoOAuthUserInfoResponse(Long id, Map<String, String> properties) {
        this.id = id;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
