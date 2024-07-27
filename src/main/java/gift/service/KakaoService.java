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
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private final KakaoAuthProvider kakaoAuthProvider;
    private final MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(KakaoService.class);

    public String getAccessToken(String authorizationCode) {
        String url = kakaoAuthProvider.getTokenRequestUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", KakaoAuthProvider.GRANT_TYPE);
        body.add("client_id", kakaoAuthProvider.getClientId());
        body.add("redirect_uri", kakaoAuthProvider.getRedirectUri());
        body.add("code", authorizationCode);

        RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        RestTemplate restTemplate = kakaoAuthProvider.getRestTemplate();

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
            logger.info("Kakao token response: {}", response);

            if(!response.getStatusCode().is2xxSuccessful()) {
                throw new CustomException.GenericException("Failed to get access token");
            }
            Map<String, Object> responseBody = response.getBody();
            if(responseBody == null || responseBody.get("access_token") == null) {
                throw new CustomException.GenericException("Invalid access token response");
            }
            return responseBody.get("access_token").toString();

        } catch (Exception ex) {
            logger.error("Failed to get access token", ex);
            throw new CustomException.GenericException("Failed to get access token: " + ex.getMessage());
        }
    }

    public Member getMember(String accessToken) {
        String url = kakaoAuthProvider.getMemberInfoRequestUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = kakaoAuthProvider.getRestTemplate();

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<>() {});
            logger.info("Kakao token response: {}", response);

            if(!response.getStatusCode().is2xxSuccessful()) {
                throw new CustomException.GenericException("Failed to get user");
            }
            Map<String, Object> responseBody = response.getBody();
            if(responseBody == null || !responseBody.containsKey("id") || !responseBody.containsKey("properties") || !responseBody.containsKey("kakao_account")) {
                throw new CustomException.GenericException("Invalid user response");
            }

            Map<String, Object> properties;
            if (responseBody.get("properties") instanceof Map) {
                properties = (Map<String, Object>) responseBody.get("properties");
            } else {
                throw new CustomException.GenericException("Invalid properties type");
            }

            Map<String, Object> kakaoAccount;
            if (responseBody.get("kakao_account") instanceof Map) {
                kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
            } else {
                throw new CustomException.GenericException("Invalid kakao_account type");
            }

            String kakaoId = responseBody.get("kakao_id").toString();
            String nickname = properties.get("nickname").toString();
            String email = (String) kakaoAccount.get("email");

            Optional<Member> memberOpt = memberRepository.findByEmail(email);
            Member member;
            if(memberOpt.isPresent()) {
                member = memberOpt.get();
                member.updateKakaoInfo(kakaoId, nickname);
            } else {
                member = new Member.Builder().email(email).password("").kakaoId(kakaoId).nickname(nickname).build();
            }
            return member;
        } catch (Exception ex) {
            logger.error("Failed to get member", ex);
            throw new CustomException.GenericException("Failed to get member: " + ex.getMessage());
        }
    }
}
