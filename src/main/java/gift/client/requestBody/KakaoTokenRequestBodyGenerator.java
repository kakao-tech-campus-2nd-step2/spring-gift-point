package gift.client.requestBody;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class KakaoTokenRequestBodyGenerator {

    private static final String GRANT_TYPE = "authorization_code";

    private final String clientId;
    private final String code;
    private final String redirectUri;

    public KakaoTokenRequestBodyGenerator(String clientId, String redirectUri, String code) {
        this.clientId = clientId;
        this.code = code;
        this.redirectUri = redirectUri;
    }

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_id", clientId);

        return params;
    }
}
