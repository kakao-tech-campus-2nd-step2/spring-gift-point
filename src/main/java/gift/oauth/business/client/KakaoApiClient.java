package gift.oauth.business.client;

import gift.global.util.StringUtils;
import gift.oauth.business.dto.OAuthParam;
import gift.global.domain.OAuthProvider;
import gift.oauth.business.dto.OAuthInfo;
import gift.oauth.business.dto.OAuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoApiClient implements OAuthApiClient {

    private final RestClient restClient;
    private final Logger log = LoggerFactory.getLogger(KakaoApiClient.class);

    public KakaoApiClient(ClientHttpRequestFactory clientHttpRequestFactory) {
        this.restClient = RestClient.builder()
            .requestFactory(clientHttpRequestFactory)
            .build();
    }

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public OAuthToken.Common getOAuthToken(OAuthParam param) {
        var url = "https://kauth.kakao.com/oauth/token";

        var result = restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(param.getTokenRequestBody())
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                (request, response) -> {
                    log.error("Error Response Body: {}", StringUtils.convert(response.getBody()));
                    throw new RuntimeException("Failed to get access token from Kakao API.");
                }
            )
            .body(OAuthToken.Kakao.class);

        log.info("Kakao response: {}", result);

        return result.toCommon();
    }

    @Override
    public String getEmail(String accessToken, OAuthParam param) {
        var url = "https://kapi.kakao.com/v2/user/me";

        var result = restClient.post()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(param.getEmailRequestBody())
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                (request, response) -> {
                    log.error("Error Response Body: {}", StringUtils.convert(response.getBody()));
                    throw new RuntimeException("Failed to get email from Kakao API.");
                }
            )
            .body(OAuthInfo.Kakao.class);

        return result.kakao_account().email();
    }
}
