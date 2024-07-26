package gift.oauth.business.dto;

import gift.global.domain.OAuthProvider;
import gift.oauth.presentation.config.KakaoConfig;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class KakaoParam implements OAuthParam {
    private final String grantType;
    private final String clientId;
    private final String redirectUri;
    private final String code;

    public KakaoParam(KakaoConfig kakaoConfig, String code) {
        this.grantType = kakaoConfig.grantType();
        this.clientId = kakaoConfig.clientId();
        this.redirectUri = kakaoConfig.redirectUri();
        this.code = code;
    }

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> getTokenRequestBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        return body;
    }

    @Override
    public MultiValueMap<String, String> getEmailRequestBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\"]");
        return body;
    }
}
