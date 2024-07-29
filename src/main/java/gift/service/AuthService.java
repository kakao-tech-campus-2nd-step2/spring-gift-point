package gift.service;

import static gift.controller.auth.AuthMapper.toTokenRequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoConfig;
import gift.controller.auth.KakaoMemberInfoResponse;
import gift.controller.auth.KakaoTokenResponse;
import gift.controller.auth.LoginRequest;
import gift.controller.auth.Token;
import gift.domain.Member;
import gift.exception.FailedHashException;
import gift.exception.KakaoTokenInvalidException;
import gift.exception.MemberNotExistsException;
import gift.exception.PasswordNotMatchedException;
import gift.exception.UnauthenticatedException;
import gift.repository.MemberRepository;
import gift.util.HashUtil;
import gift.util.JwtUtil;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final KakaoTokenService kakaoTokenService;
    private final KakaoConfig kakaoConfig;

    public AuthService(MemberRepository memberRepository, KakaoTokenService kakaoTokenService,
        KakaoConfig kakaoConfig) {
        this.memberRepository = memberRepository;
        this.kakaoTokenService = kakaoTokenService;
        this.kakaoConfig = kakaoConfig;
    }

    public void saveKakaoToken(UUID memberId, KakaoTokenResponse token) {
        kakaoTokenService.save(memberId, token);
    }

    public KakaoTokenResponse getToken(UUID memberId) {
        return kakaoTokenService.findAccessTokenByMemberId(memberId);
    }

    public Token login(LoginRequest member) {
        Member m = memberRepository.findByEmail(member.email())
            .orElseThrow(MemberNotExistsException::new);
        String encryptedPassword;
        try {
            encryptedPassword = HashUtil.hashPassword(member.rawPassword());
        } catch (Exception e) {
            throw new FailedHashException();
        }
        if (!encryptedPassword.equals(m.getPassword())) {
            throw new PasswordNotMatchedException();
        }
        return new Token(JwtUtil.generateToken(m.getId(), m.getEmail()));
    }

    public String getAuthorizationUrl() {
        return String.format(
            "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s",
            kakaoConfig.getKakaoAppKey(), kakaoConfig.getRedirectUri());
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
}