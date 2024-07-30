package gift.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {
    
    private String apiKey;
    private String redirectUri;

    public String getApiKey() {
        return apiKey;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

}