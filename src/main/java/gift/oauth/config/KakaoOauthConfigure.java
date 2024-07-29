package gift.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kakao")
public class KakaoOauthConfigure {

    private String clientId;

    private String redirectURL;

    private String authorizeCodeURL;

    private String tokenURL;

    private String userInfoFromAccessTokenURL;

    private String messageSendURL;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getAuthorizeCodeURL() {
        return authorizeCodeURL;
    }

    public void setAuthorizeCodeURL(String authorizeCodeURL) {
        this.authorizeCodeURL = authorizeCodeURL;
    }

    public String getTokenURL() {
        return tokenURL;
    }

    public void setTokenURL(String tokenURL) {
        this.tokenURL = tokenURL;
    }

    public String getUserInfoFromAccessTokenURL() {
        return userInfoFromAccessTokenURL;
    }

    public void setUserInfoFromAccessTokenURL(String userInfoFromAccessTokenURL) {
        this.userInfoFromAccessTokenURL = userInfoFromAccessTokenURL;
    }

    public String getMessageSendURL() {
        return messageSendURL;
    }

    public void setMessageSendURL(String messageSendURL) {
        this.messageSendURL = messageSendURL;
    }
}
