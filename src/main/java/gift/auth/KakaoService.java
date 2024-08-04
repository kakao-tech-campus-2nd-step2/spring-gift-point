package gift.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.type.InvalidTokenException;
import gift.member.domain.Member;
import gift.member.domain.MemberRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KakaoService {
    private final KakaoOauthProperty kakaoOauthProperty;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;

    public KakaoService(KakaoOauthProperty kakaoOauthProperty,
                        RestClient restClient,
                        ObjectMapper objectMapper,
                        MemberRepository memberRepository
    ) {
        this.kakaoOauthProperty = kakaoOauthProperty;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.memberRepository = memberRepository;
    }

    public String getKakaoRedirectUrl() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoOauthProperty.clientId())
                .queryParam("redirect_uri", kakaoOauthProperty.redirectUri())
                .queryParam("scope", String.join(",", kakaoOauthProperty.scope()).replace("\"", ""))
                .toUriString();
    }

    public KakaoToken fetchToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", kakaoOauthProperty.clientId());
        formData.add("redirect_uri", kakaoOauthProperty.redirectUri());
        formData.add("code", code);

        return restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(KakaoToken.class);
    }

    public KakaoToken refreshToken(String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", kakaoOauthProperty.clientId());
        formData.add("refresh_token", refreshToken);

        return restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(KakaoToken.class);
    }

    public void saveToken(KakaoToken kakaoToken, Member member) {
        member.updateKakaoTokens(
                kakaoToken.accessToken(),
                kakaoToken.refreshToken(),
                LocalDateTime.now().plusSeconds(kakaoToken.expiresIn()),
                LocalDateTime.now().plusSeconds(kakaoToken.refreshTokenExpiresIn())
        );
        memberRepository.save(member);
    }

    public KakaoResponse fetchMemberInfo(String accessToken) {
        return restClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(KakaoResponse.class);
    }

    public void unlink(Long userId) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("target_id_type", "user_id");
        formData.add("target_id", String.valueOf(userId));

        restClient.post()
                .uri("https://kapi.kakao.com/v1/user/unlink")
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoOauthProperty.adminKey())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(String.class);
    }

    public String sendOrderMessage(Long kakaoId, KakaoMessageSend form, String accessToken) {
        JsonNode receiverUuidsNode = objectMapper.valueToTree(List.of(kakaoId.toString()));
        JsonNode templateObjectNode = objectMapper.valueToTree(form);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("receiver_uuids", receiverUuidsNode.toString());
        formData.add("template_object", templateObjectNode.toString());

        return restClient.post()
                .uri("https://kapi.kakao.com/v2/api/talk/memo/default/send")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(String.class);
    }

    public String getValidAccessToken(Member member) {
        LocalDateTime now = LocalDateTime.now();

        if (member.getKakaoAccessTokenExpiresAt().isBefore(now)) {
            validateRefreshTokenExpiry(member, now);

            KakaoToken newToken = refreshToken(member.getKakaoRefreshToken());
            member.updateKakaoTokens(
                    newToken.accessToken(),
                    newToken.refreshToken(),
                    now.plusSeconds(newToken.expiresIn()),
                    now.plusSeconds(newToken.refreshTokenExpiresIn())
            );
            memberRepository.save(member);
            return newToken.accessToken();
        }

        return member.getKakaoAccessToken();
    }

    private static void validateRefreshTokenExpiry(Member member, LocalDateTime now) {
        if (member.getKakaoRefreshTokenExpiresAt().isBefore(now)) {
            throw new InvalidTokenException("카카오 토큰 갱신이 불가능합니다.");
        }
    }
}
