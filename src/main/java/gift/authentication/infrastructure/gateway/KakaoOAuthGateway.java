package gift.authentication.infrastructure.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.authentication.service.OAuthGateway;
import gift.authentication.service.OAuthResult;
import gift.core.domain.authentication.OAuthType;
import gift.core.domain.authentication.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoOAuthGateway implements OAuthGateway {
    @Value("${oauth2.client.kakao.client-id}")
    private String clientId;

    @Value("${oauth2.client.kakao.client-secret}")
    private String clientSecret;

    @Value("${oauth2.client.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.client.kakao.token-uri}")
    private String tokenUri;

    private final RestClient restClient;

    public KakaoOAuthGateway(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public OAuthResult authenticate(String code) {
        KakaoTokenResponse tokenResponse = requestToken(code);
        return new OAuthResult(tokenResponse.accessToken());
    }

    @Override
    public OAuthType getType() {
        return OAuthType.KAKAO;
    }

    private KakaoTokenResponse requestToken(String code) {
        return restClient
                .post()
                .uri(tokenUri)
                .headers(headers -> headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .body(buildParams(code))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new AuthenticationFailedException();
                })
                .body(KakaoTokenResponse.class);
    }

    private MultiValueMap<String, String> buildParams(String code) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", clientSecret);
        return params;
    }

    private record KakaoTokenResponse(@JsonProperty("access_token") String accessToken) {}
}
