package gift.service.oauth;

import static gift.util.constants.KakaoOAuthConstants.SCOPES_FAILURE_ERROR;
import static gift.util.constants.KakaoOAuthConstants.TOKEN_FAILURE_ERROR;
import static gift.util.constants.KakaoOAuthConstants.TOKEN_RESPONSE_ERROR;
import static gift.util.constants.KakaoOAuthConstants.UNLINK_FAILURE_ERROR;
import static gift.util.constants.KakaoOAuthConstants.UNLINK_RESPONSE_ERROR;
import static gift.util.constants.KakaoOAuthConstants.USERINFO_FAILURE_ERROR;
import static gift.util.constants.KakaoOAuthConstants.USERINFO_RESPONSE_ERROR;

import gift.config.KakaoProperties;
import gift.dto.member.MemberLoginRequest;
import gift.dto.member.MemberRegisterRequest;
import gift.dto.member.MemberResponse;
import gift.dto.oauth.KakaoScopeResponse;
import gift.dto.oauth.KakaoTokenResponse;
import gift.dto.oauth.KakaoUnlinkResponse;
import gift.dto.oauth.KakaoUserResponse;
import gift.exception.member.EmailAlreadyUsedException;
import gift.exception.oauth.KakaoScopeException;
import gift.exception.oauth.KakaoTokenException;
import gift.exception.oauth.KakaoUnlinkException;
import gift.exception.oauth.KakaoUserInfoException;
import gift.service.MemberService;
import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class KakaoOAuthService {

    private final KakaoProperties kakaoProperties;
    private final RestClient client;
    private final MemberService memberService;

    @Value("${kakao.password}")
    private String kakaoPassword;

    public KakaoOAuthService(KakaoProperties kakaoProperties, RestClient client, MemberService memberService) {
        this.kakaoProperties = kakaoProperties;
        this.client = client;
        this.memberService = memberService;
    }

    public KakaoTokenResponse getAccessToken(String code) {
        String kakaoTokenUrl = "https://kauth.kakao.com/oauth/token";
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", kakaoProperties.clientId());
        requestBody.add("redirect_uri", kakaoProperties.redirectUrl());
        requestBody.add("code", code);

        try {
            ResponseEntity<Map<String, Object>> response = client.post()
                .uri(URI.create(kakaoTokenUrl))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(requestBody)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                });
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null) {
                String accessToken = (String) responseBody.get("access_token");
                Integer expiresIn = (Integer) responseBody.get("expires_in");
                String refreshToken = (String) responseBody.get("refresh_token");
                Integer refreshTokenExpiresIn = (Integer) responseBody.get("refresh_token_expires_in");

                return new KakaoTokenResponse(accessToken, expiresIn, refreshToken, refreshTokenExpiresIn);
            } else {
                throw new KakaoTokenException(TOKEN_RESPONSE_ERROR);
            }
        } catch (RestClientResponseException e) {
            throw new KakaoTokenException(TOKEN_FAILURE_ERROR);
        }
    }

    public KakaoUnlinkResponse unlinkUser(String accessToken) {
        String kakaoUnlinkUrl = "https://kapi.kakao.com/v1/user/unlink";

        try {
            ResponseEntity<Map<String, Object>> response = client.post()
                .uri(URI.create(kakaoUnlinkUrl))
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                });
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null) {
                Long id = ((Number) responseBody.get("id")).longValue();
                return new KakaoUnlinkResponse(id);
            } else {
                throw new KakaoUnlinkException(UNLINK_RESPONSE_ERROR);
            }
        } catch (RestClientResponseException e) {
            throw new KakaoUnlinkException(UNLINK_FAILURE_ERROR);
        }
    }

    public KakaoScopeResponse getUserScopes(String accessToken) {
        String kakaoScopesUrl = "https://kapi.kakao.com/v2/user/scopes";

        try {
            ResponseEntity<KakaoScopeResponse> response = client.get()
                .uri(URI.create(kakaoScopesUrl))
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .toEntity(KakaoScopeResponse.class);

            return response.getBody();
        } catch (RestClientResponseException e) {
            throw new KakaoScopeException(SCOPES_FAILURE_ERROR);
        }
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        String kakaoUserInfoUrl = "https://kapi.kakao.com/v2/user/me";

        try {
            ResponseEntity<Map<String, Object>> response = client.get()
                .uri(URI.create(kakaoUserInfoUrl))
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                });
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null) {
                Long id = ((Number) responseBody.get("id")).longValue();
                Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
                String nickname = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname");
                String email = (String) kakaoAccount.get("email");

                return new KakaoUserResponse(id, nickname, email);
            } else {
                throw new KakaoUserInfoException(USERINFO_RESPONSE_ERROR);
            }
        } catch (RestClientResponseException e) {
            throw new KakaoUserInfoException(USERINFO_FAILURE_ERROR);
        }
    }

    public MemberResponse registerOrLoginKakaoUser(KakaoUserResponse userResponse) {
        try {
            MemberRegisterRequest registerRequest = new MemberRegisterRequest(userResponse.email(), kakaoPassword);
            return memberService.registerMember(registerRequest);
        } catch (EmailAlreadyUsedException e) {
            return memberService.loginKakaoMember(userResponse.email());
        }
    }
}
