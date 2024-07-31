package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.JwtProvider;
import gift.domain.member.Member;
import gift.domain.member.SocialAccount;
import gift.domain.member.SocialType;
import gift.repository.MemberRepository;
import gift.service.kakao.KakaoApiService;
import gift.service.kakao.KakaoTokenInfoResponse;
import gift.service.kakao.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class KakaoLoginServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private KakaoApiService kakaoApiService;

    @InjectMocks
    private KakaoLoginService kakaoLoginService;

    @DisplayName("Redirect URL 을 header location 에 넣고 헤더를 반환한다.")
    @Test
    void getRedirectHeaders() throws Exception {
        //given
        String redirectUrl = "https://test.com";
        String clientId = "test-client-id";

        String loginUrl = "https://kauth.kakao.com/oauth/authorize?&response_type=code"
                + "&redirect_uri=" + redirectUrl
                + "&client_id=" + clientId;

        given(kakaoApiService.createLoginUrl()).willReturn(loginUrl);

        //when
        HttpHeaders headers = kakaoLoginService.getRedirectHeaders();

        //then
        assertThat(headers.getLocation().toString()).isEqualTo(loginUrl);
        then(kakaoApiService).should().createLoginUrl();
    }

    @DisplayName("카카오톡 로그인을 수행한다. 로그인이 완료되면 JWT 를 발행해 응답한다.")
    @Test
    void processKakaoAuth() throws Exception {
        //given
        String jwt = "test-jwt";
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        KakaoTokenInfoResponse tokenInfo = new KakaoTokenInfoResponse(accessToken, refreshToken);

        JsonNode jsonNode = new ObjectMapper().readTree("{ \"id\": 123, \"properties\": { \"nickname\": \"test-nickname\" } }");

        given(kakaoApiService.getTokenInfo(anyString())).willReturn(tokenInfo);
        given(kakaoApiService.getUserInfo(anyString())).willReturn(ResponseEntity.ok().body("test-body"));
        given(kakaoApiService.parseJson(anyString())).willReturn(jsonNode);

        SocialAccount socialAccount = new SocialAccount(SocialType.KAKAO, 123L, accessToken, refreshToken);
        Member member = new Member.MemberBuilder().socialAccount(socialAccount).build();
        given(memberRepository.findBySocialAccount_SocialIdAndSocialAccount_SocialType(anyLong(), any(SocialType.class))).willReturn(Optional.of(member));
        given(jwtProvider.create(any(Member.class))).willReturn(jwt);

        //when
        TokenResponse tokenResponse = kakaoLoginService.processKakaoAuth("test-code");

        //then
        assertThat(tokenResponse.getToken()).isEqualTo(jwt);

        then(kakaoApiService).should().getTokenInfo(anyString());
        then(kakaoApiService).should().getUserInfo(anyString());
        then(kakaoApiService).should().parseJson(anyString());
        then(memberRepository).should().findBySocialAccount_SocialIdAndSocialAccount_SocialType(anyLong(), any(SocialType.class));
        then(jwtProvider).should().create(any(Member.class));
    }

}
