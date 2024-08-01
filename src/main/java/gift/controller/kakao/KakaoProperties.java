package gift.controller.kakao;

import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {

    private final String clientId;
    String redirectUri;
    String authUri;
    String tokenUri;
    String userUri;
    String messageUri;

    @ConstructorBinding
    public KakaoProperties(
        String clientId, String redirectUri, String authUri,
        String tokenUri, String userUri, String messageUri
    ) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.authUri = authUri;
        this.tokenUri = tokenUri;
        this.userUri = userUri;
        this.messageUri = messageUri;
    }

    public URI getLoginUri() {
        var loginUri = ServletUriComponentsBuilder.fromUriString(authUri)
            .queryParam("response_type", "code")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUri)
            .build().toUri();
        return loginUri;
    }

    public URI getUserUri() {
        return ServletUriComponentsBuilder.fromUriString(userUri).build().toUri();
    }

    public String getClientId() {
        return clientId;
    }

    public URI getRedirectUri() {
        return ServletUriComponentsBuilder.fromUriString(redirectUri).build().toUri();
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public String getMessageUri() {
        return messageUri;
    }
}
