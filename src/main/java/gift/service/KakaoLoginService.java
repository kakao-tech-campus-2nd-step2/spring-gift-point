package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.oauth2.OAuth2Exception;
import gift.exception.oauth2.OAuth2TokenException;
import gift.model.OAuth2AccessToken;
import gift.repository.OAuth2AccessTokenRepository;
import gift.response.oauth2.OAuth2MemberInfoResponse;
import gift.response.oauth2.OAuth2TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
public class KakaoLoginService implements OAuth2LoginService {

    @Value("${kakao.token-request-uri}")
    public String TOKEN_REQUEST_URI = "";
    @Value("${kakao.member-info-request-uri}")
    public String MEMBER_INFO_REQUEST_URI = "";
    @Value("${kakao.auth-error}")
    public String AUTH_ERROR = "error";
    @Value("${kakao.auth-error-description}")
    public String AUTH_ERROR_DESCRIPTION = "error_description";

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final WebClient client;
    private final OAuth2AccessTokenRepository accessTokenRepository;

    public KakaoLoginService(WebClient client,
        OAuth2AccessTokenRepository accessTokenRepository) {
        this.client = client;
        this.accessTokenRepository = accessTokenRepository;
    }

    public void checkRedirectUriParams(HttpServletRequest request) {
        if (request.getParameterMap().containsKey(AUTH_ERROR) || request.getParameterMap()
            .containsKey(AUTH_ERROR_DESCRIPTION)) {
            String error = request.getParameter(AUTH_ERROR);
            String errorDescription = request.getParameter(AUTH_ERROR_DESCRIPTION);
            throw new OAuth2Exception(error, errorDescription);
        }
    }

    public OAuth2TokenResponse getToken(String code) {
        try {
            return client.post()
                .uri(URI.create(TOKEN_REQUEST_URI))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(Mono.just(createTokenRequest(clientId, redirectUri, code)),
                    LinkedMultiValueMap.class)
                .retrieve()
                .bodyToMono(OAuth2TokenResponse.class)
                .retry(3)
                .block();
        } catch (WebClientResponseException e) {
            throw new OAuth2TokenException(e);
        }
    }

    public String getMemberInfo(String accessToken) {

        try {
            OAuth2MemberInfoResponse response = client.get()
                .uri(URI.create(MEMBER_INFO_REQUEST_URI))
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(OAuth2MemberInfoResponse.class)
                .retry(3)
                .block();

            return Optional.ofNullable(response)
                .map(OAuth2MemberInfoResponse::id)
                .orElseThrow(() -> new OAuth2TokenException("Member ID is null"));

        } catch (WebClientResponseException e) {
            throw new OAuth2TokenException(e);
        }
    }

    @Transactional
    public void saveAccessToken(Long memberId, String accessToken) {
        accessTokenRepository.findByMemberId(memberId)
            .ifPresentOrElse(token -> token.updateToken(accessToken),
                () -> accessTokenRepository.save(new OAuth2AccessToken(memberId, accessToken))
            );
    }

    public LinkedMultiValueMap<String, String> createTokenRequest(String clientId,
        String redirectUri, String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        return body;
    }

}
