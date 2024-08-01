package gift.service;

import gift.client.KakaoApiClient;
import gift.config.KakaoProperties;
import gift.dto.KakaoMemberResponse;
import gift.dto.KakaoTokenResponse;
import gift.model.KakaoMember;
import gift.repository.KakaoMemberRepository;
import gift.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class KakaoAuthService {

    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthService.class);

    private final KakaoApiClient kakaoApiClient;
    private final KakaoProperties kakaoProperties;
    private final JwtUtil jwtUtil;
    private final KakaoMemberRepository kakaoMemberRepository;
    private final RestTemplate restTemplate;

    public KakaoAuthService(KakaoApiClient kakaoApiClient, KakaoProperties kakaoProperties, JwtUtil jwtUtil, KakaoMemberRepository kakaoMemberRepository, RestTemplate restTemplate) {
        this.kakaoApiClient = kakaoApiClient;
        this.kakaoProperties = kakaoProperties;
        this.jwtUtil = jwtUtil;
        this.kakaoMemberRepository = kakaoMemberRepository;
        this.restTemplate = restTemplate;
    }

    public KakaoTokenResponse getAccessToken(String code) {
        return kakaoApiClient.getAccessToken(code);
    }

    public String getAuthorizationUri() {
        return UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoProperties.getClientId())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
                .toUriString();
    }

    public String handleKakaoLogin(String accessToken) {
        try {
            String uniqueId = getUserId(accessToken);

            Optional<KakaoMember> existingMember = kakaoMemberRepository.findByUniqueId(uniqueId);

            KakaoMember kakaoMember;
            if (existingMember.isPresent()) {
                kakaoMember = existingMember.get().updateKakaoAccessToken(accessToken);
            } else {
                kakaoMember = KakaoMember.createWithKakaoAccessToken(uniqueId, accessToken);
            }
            kakaoMemberRepository.save(kakaoMember);

            return jwtUtil.generateToken(uniqueId);
        } catch (Exception e) {
            logger.error("Failed to handle Kakao login", e);
            throw new RuntimeException("Failed to handle Kakao login", e);
        }
    }

    private String getUserId(String accessToken) {
        try {
            String url = "https://kapi.kakao.com/v2/user/me";
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<Object> kakaoProfileRequest = new HttpEntity<>(headers);
            ResponseEntity<KakaoMemberResponse> response = restTemplate.exchange(url, HttpMethod.POST, kakaoProfileRequest, KakaoMemberResponse.class);

            KakaoMemberResponse memberInfo = response.getBody();

            if (memberInfo != null && memberInfo.getId() != null) {
                return memberInfo.getId().toString();
            } else {
                throw new RuntimeException("Failed to get user info from Kakao");
            }
        } catch (Exception e) {
            logger.error("Failed to get user info from Kakao", e);
            throw new RuntimeException("Failed to get user info from Kakao", e);
        }
    }
}
