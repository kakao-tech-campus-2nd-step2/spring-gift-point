package gift.service;

import static gift.controller.auth.AuthMapper.toKakaoTokenResponse;
import static gift.controller.auth.AuthMapper.toTokenRequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoConfig;
import gift.controller.auth.KakaoMemberInfoResponse;
import gift.controller.auth.KakaoTokenResponse;
import gift.domain.KakaoToken;
import gift.exception.KakaoTokenInvalidException;
import gift.exception.UnauthenticatedException;
import gift.repository.KakaoTokenRepository;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
public class KakaoTokenService {

    private final KakaoTokenRepository kakaoTokenRepository;
    private final KakaoConfig kakaoConfig;

    public KakaoTokenService(KakaoTokenRepository kakaoTokenRepository, KakaoConfig kakaoConfig) {
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.kakaoConfig = kakaoConfig;
    }

    public KakaoMemberInfoResponse getMemberInfo(KakaoTokenResponse kakaoToken) {
        final RestClient client = RestClient.builder().build();
        ResponseEntity<String> response;
        JsonNode rootNode;
        try {
            response = client.post()
                .uri(KakaoConfig.getRequestMemberInfoUrl())
                .header("Authorization", "Bearer " + kakaoToken.getAccessToken())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .toEntity(String.class);
            rootNode = new ObjectMapper().readTree(response.getBody());
        } catch (Exception ex) {
            throw new KakaoTokenInvalidException("cannot load kakao member profile");
        }
        return new KakaoMemberInfoResponse(
            rootNode.path("kakao_account").path("email").asText(),
            rootNode.path("properties").path("nickname").asText());
    }

    public KakaoTokenResponse getKakaoToken(String authorizationCode) {
        final RestClient client = RestClient.builder().build();
        ResponseEntity<String> response;
        JsonNode jsonNode;
        try {
            response = client.post().uri(URI.create(KakaoConfig.getTokenUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(toTokenRequestBody(KakaoConfig.getGrantType(), kakaoConfig.getKakaoAppKey(),
                    kakaoConfig.getRedirectUri(), authorizationCode)).retrieve()
                .toEntity(String.class);
            jsonNode = new ObjectMapper().readTree(response.getBody());
        } catch (Exception ex) {
            throw new UnauthenticatedException("cannot get Kakao token");
        }
        return new KakaoTokenResponse(jsonNode.get("access_token").asText());
    }

    public String getAuthorizationUrl() {
        return String.format(
            "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s",
            kakaoConfig.getKakaoAppKey(), kakaoConfig.getRedirectUri());
    }

    @Transactional(readOnly = true)
    public KakaoTokenResponse findAccessTokenByMemberId(UUID memberId) {
        return toKakaoTokenResponse(kakaoTokenRepository.findByMemberId(memberId)
            .orElseThrow(KakaoTokenInvalidException::new));
    }

    @Transactional
    public void save(UUID memberId, KakaoTokenResponse kakaoToken) {
        kakaoTokenRepository.save(new KakaoToken(memberId, kakaoToken.getAccessToken()));
    }
}
