package gift.service;

import gift.entity.Member;
import gift.exception.CustomException;
import gift.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoService.class);
    private final KakaoAuthProvider kakaoAuthProvider;
    private final MemberRepository memberRepository;

    public String getAccessToken(String authorizationCode) {
        logger.debug("Requesting access token with authorization code: {}", authorizationCode);

        if (authorizationCode == null || authorizationCode.isEmpty()) {
            throw new CustomException.GenericException("Authorization code is missing");
        }

        RequestEntity<MultiValueMap<String, String>> requestEntity = createTokenRequestEntity(authorizationCode);

        try {
            ResponseEntity<Map<String, Object>> response = kakaoAuthProvider.getRestTemplate().exchange(requestEntity, new ParameterizedTypeReference<>() {});
            return handleTokenResponse(response);
        } catch (Exception ex) {
            logger.error("Failed to get access token", ex);
            throw new CustomException.GenericException("Failed to get access token: " + ex.getMessage());
        }
    }

    private RequestEntity<MultiValueMap<String, String>> createTokenRequestEntity(String authorizationCode) {
        String url = kakaoAuthProvider.getTokenRequestUri();
        logger.debug("Token request URI: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", KakaoAuthProvider.GRANT_TYPE);
        body.add("client_id", kakaoAuthProvider.getClientId());
        body.add("redirect_uri", kakaoAuthProvider.getRedirectUri());
        body.add("code", authorizationCode);

        logger.debug("Request body: {}", body);

        return new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
    }

    private String handleTokenResponse(ResponseEntity<Map<String, Object>> response) {
        logger.info("Kakao token response: {}", response);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CustomException.GenericException("Failed to get access token");
        }

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("access_token") || responseBody.get("access_token") == null) {
            logger.error("Invalid access token response: {}", responseBody);
            throw new CustomException.GenericException("Invalid access token response");
        }

        logger.debug("Successfully retrieved access token: {}", responseBody.get("access_token").toString());
        return responseBody.get("access_token").toString();
    }

    public Member getMember(String accessToken) {
        logger.debug("Requesting member info with access token: {}", accessToken);

        if (accessToken == null || accessToken.isEmpty()) {
            throw new CustomException.GenericException("Access token is missing");
        }

        HttpEntity<String> request = createMemberInfoRequestEntity(accessToken);

        try {
            ResponseEntity<Map<String, Object>> response = kakaoAuthProvider.getRestTemplate().exchange(
                    kakaoAuthProvider.getMemberInfoRequestUri(),
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<>() {}
            );

            logger.debug("Response: {}", response);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new CustomException.GenericException("Failed to get user");
            }

            return handleMemberResponse(response.getBody());
        } catch (Exception ex) {
            logger.error("Failed to get member", ex);
            throw new CustomException.GenericException("Failed to get member: " + ex.getMessage());
        }
    }

    private HttpEntity<String> createMemberInfoRequestEntity(String accessToken) {
        String url = kakaoAuthProvider.getMemberInfoRequestUri();
        logger.debug("Member info request URI: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        return new HttpEntity<>(headers);
    }

    private Member handleMemberResponse(Map<String, Object> responseBody) {
        if (responseBody == null || !responseBody.containsKey("id") || !responseBody.containsKey("properties") || !responseBody.containsKey("kakao_account")) {
            logger.error("Invalid user response: {}", responseBody);
            throw new CustomException.GenericException("Invalid user response");
        }

        Map<String, Object> properties = extractMapFromResponse(responseBody, "properties");
        Map<String, Object> kakaoAccount = extractMapFromResponse(responseBody, "kakao_account");

        String kakaoId = responseBody.get("id").toString();
        String nickname = properties.get("nickname") != null ? properties.get("nickname").toString() : "";
        String email = kakaoAccount.get("email") != null ? kakaoAccount.get("email").toString() : "";

        Optional<Member> memberOpt = memberRepository.findByEmail(email);
        Member member = memberOpt.map(m -> updateExistingMember(m, kakaoId, nickname)).orElseGet(() -> createNewMember(email, kakaoId, nickname));

        logger.debug("Successfully retrieved member: {}", member);
        return member;
    }

    private Map<String, Object> extractMapFromResponse(Map<String, Object> responseBody, String key) {
        Object value = responseBody.get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        } else {
            throw new CustomException.GenericException("Invalid " + key + " type");
        }
    }

    private Member updateExistingMember(Member member, String kakaoId, String nickname) {
        member.updateKakaoInfo(kakaoId, nickname);
        return member;
    }

    private Member createNewMember(String email, String kakaoId, String nickname) {
        return new Member.Builder()
                .email(email)
                .password("")
                .kakaoId(kakaoId)
                .nickname(nickname)
                .build();
    }
}
