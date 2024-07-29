package gift.oauth;

import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@ConfigurationProperties(prefix = "kakao")
public class KakaoApiSecurityProperties {

    private final String clientId;

    private final String redirectUri;

    private final Uri url;

    @ConstructorBinding
    public KakaoApiSecurityProperties(String clientId, String redirectUri, Uri url) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.url = url;
    }

    public static class Uri {

        private final String login;
        private final String token;
        private final String userInfo;
        private final String memoSend;

        @ConstructorBinding
        public Uri(String login, String token, String userInfo, String memoSend) {
            this.login = login;
            this.token = token;
            this.userInfo = userInfo;
            this.memoSend = memoSend;
        }

        public String getLogin() {
            return login;
        }

        public String getToken() {
            return token;
        }

        public String getMemoSend() {
            return memoSend;
        }

        public String getUserInfo() {
            return userInfo;
        }
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public URI getLoginUri() {
        var loginUri = ServletUriComponentsBuilder.fromUriString(url.getLogin())
            .queryParam("response_type", "code")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUri)
            .build().toUri();
        return loginUri;
    }

    public URI getTokenUri() {
        return URI.create(url.getToken());
    }

    public URI getUserInfoUri() {
        return URI.create(url.getUserInfo());
    }

    public URI getMemoSend() {
        return URI.create(url.getMemoSend());
    }
}
