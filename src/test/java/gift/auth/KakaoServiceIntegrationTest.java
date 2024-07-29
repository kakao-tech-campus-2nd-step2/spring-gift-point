package gift.auth;

import gift.exception.type.InvalidTokenException;
import gift.member.domain.Member;
import gift.member.domain.MemberRepository;
import gift.member.domain.OauthProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("classpath:application.properties")
class KakaoServiceIntegrationTest {

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private KakaoOauthProperty kakaoOauthProperty;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void getKakaoRedirectUrl_통합테스트() {
        // When
        String redirectUrl = kakaoService.getKakaoRedirectUrl();

        System.out.println("redirectUrl = " + redirectUrl);

        // Then
        assertThat(redirectUrl).isEqualTo("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" +
                kakaoOauthProperty.clientId() + "&redirect_uri=" + kakaoOauthProperty.redirectUri() + "&scope=" + String.join(
                ",",
                kakaoOauthProperty.scope()).replace("\"", ""));
    }

    @Test
    void fetchToken_통합테스트() {
        // Given
        String code = "dsAikckSBofevMDWyf-Tul7fqX7T-sULH7GsiH-RvIDuf_lWWDF0vgAAAAQKKclgAAABkO8OE-L_A_o_BVb6-Q";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", kakaoOauthProperty.clientId());
        formData.add("redirect_uri", kakaoOauthProperty.redirectUri());
        formData.add("code", code);

        // When
        KakaoToken token = kakaoService.fetchToken(code);
        System.out.println("token = " + token);
        /**
         * token =
         * KakaoToken
         * [
         * tokenType=bearer,
         * accessToken=_EWAeY_38JLE5pHYpMzy29ylbFfQrenIAAAAAQo9cxcAAAGQ5Rb4nxamEcnPBcmr,
         * idToken=null,
         * expiresIn=21599,
         * refreshToken=1FSGDX9YLSIdja4gfN1C5JQh4uaqc8X3AAAAAgo9cxcAAAGQ5Rb4nBamEcnPBcmr,
         * refreshTokenExpiresIn=5183999, scope=account_email profile_nickname
         * ]
         */

        // Then
        assertThat(token).isNotNull();
        assertThat(token.accessToken()).isNotEmpty();
    }

    @Test
    void refreshToken_통합테스트() {
        // Given
        String refreshToken = "1FSGDX9YLSIdja4gfN1C5JQh4uaqc8X3AAAAAgo9cxcAAAGQ5Rb4nBamEcnPBcmr";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", kakaoOauthProperty.clientId());
        formData.add("refresh_token", refreshToken);

        // When
        KakaoToken token = kakaoService.refreshToken(refreshToken);
        System.out.println("token = " + token);

        // Then
        assertThat(token).isNotNull();
        assertThat(token.accessToken()).isNotEmpty();
    }

    @Test
    void fetchMemberInfo_통합테스트() {
        // Given
        String accessToken = "usBKw-_NO-6mF5yaScDoVq1UO0t6HAWcAAAAAQoqJY8AAAGQ7w6rVRamEcnPBcmr";

        // When
        KakaoResponse response = kakaoService.fetchMemberInfo(accessToken);
        System.out.println("response = " + response);

        /**
         * KakaoResponse
         * [
         * id=3636131132,
         * kakaoAccount=
         * KakaoAccount
         * [
         * profile=KakaoProfile[nickname=김유겸],
         * email=rladbrua0207@gmail.com
         *]
         * ]
         */

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
    }

    @Test
    void unlink_및_unlink_후_연결_통합테스트() {
        // Given
        Long userId = 3636131132L;
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("target_id_type", "user_id");
        formData.add("target_id", String.valueOf(userId));

        // When
        kakaoService.unlink(userId);

        // Then
        assertThat(userId).isNotNull();

        // unlink 후 연결 테스트
        String accessToken = "_EWAeY_38JLE5pHYpMzy29ylbFfQrenIAAAAAQo9cxcAAAGQ5Rb4nxamEcnPBcmr";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kakaoService.fetchMemberInfo(accessToken);
        });
        assertThat(exception.getMessage()).isEqualTo(
                "401 Unauthorized: \"{\"msg\":\"this access token does not exist\",\"code\":-401}\"");
    }

    @Test
    void sendOrderMessage_통합테스트() {
        // Given
        Long kakaoId = 3636131132L;
        KakaoMessageSend message = new KakaoMessageSend("text",
                "Order message",
                new KakaoMessageSend.Link("http://example.com", "http://example.com"),
                "확인하기");
        String accessToken = "usBKw-_NO-6mF5yaScDoVq1UO0t6HAWcAAAAAQoqJY8AAAGQ7w6rVRamEcnPBcmr";

        // When
        String response = kakaoService.sendOrderMessage(kakaoId, message, accessToken);
        System.out.println("response = " + response);

        // Then
        assertThat(response).isNotNull();
    }

    @Test
    void getValidAccessToken_통합테스트() {
        // Given
        Long kakaoId = 1L;
        String accesToken = "";
        String refreshToken = "";
        LocalDateTime kakaoAccessTokenExpiresAt = LocalDateTime.now();
        LocalDateTime kakaoRefreshTokenExpiresAt = LocalDateTime.now();

        Member member = new Member(
                1L,
                "test@example.com",
                OauthProvider.KAKAO,
                kakaoId,
                accesToken,
                refreshToken,
                kakaoAccessTokenExpiresAt,
                kakaoRefreshTokenExpiresAt
        );
        memberRepository.save(member);

        // When
        String validAccessToken = kakaoService.getValidAccessToken(member);

        // Then
        assertThat(validAccessToken).isNotNull();
        assertThat(validAccessToken).isNotEqualTo(accesToken); // 갱신되었으므로 다르다
    }

    @Test
    void 갱신_불가능한_토큰_통합테스트() {
        // Given
        Long kakaoId = 1L;
        String accesToken = "";
        String refreshToken = "";
        LocalDateTime kakaoAccessTokenExpiresAt = LocalDateTime.now();
        LocalDateTime kakaoRefreshTokenExpiresAt = LocalDateTime.now();

        Member member = new Member(
                1L,
                "test@example.com",
                OauthProvider.KAKAO,
                kakaoId,
                accesToken,
                refreshToken,
                kakaoAccessTokenExpiresAt,
                kakaoRefreshTokenExpiresAt
        );
        memberRepository.save(member);

        // When & Then
        assertThrows(InvalidTokenException.class, () -> {
            kakaoService.getValidAccessToken(member);
        });
    }

}
