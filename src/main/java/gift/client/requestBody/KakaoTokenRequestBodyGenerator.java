package gift.client.requestBody;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class KakaoTokenRequestBodyGenerator {

    private static final String REDIRECT_URI = "http://localhost:8080";
    private static final String GRANT_TYPE = "authorization_code";

    private String clientId;
    private String code;

    public KakaoTokenRequestBodyGenerator(String clientId, String code) {
        this.clientId = clientId;
        this.code = code;
    }

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);
        params.add("client_id", clientId);

        return params;
    }
}
