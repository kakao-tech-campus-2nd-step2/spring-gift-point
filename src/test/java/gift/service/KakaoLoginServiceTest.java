package gift.service;

import static gift.util.MemberFixture.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import gift.domain.KakaoToken;
import gift.dto.JwtResponse;
import gift.dto.MemberDTO;
import gift.repository.KakaoTokenRepository;
import gift.util.JwtProvider;
import gift.util.KakaoProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(KakaoLoginService.class)
public class KakaoLoginServiceTest {

    @MockBean
    private KakaoProperties kakaoProperties;
    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private KakaoTokenRepository kakaoTokenRepository;

    @Autowired
    private KakaoLoginService kakaoLoginService;
    @Autowired
    private MockRestServiceServer mockServer;

    private static final String KAKAO_OAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_PROFILE_URL = "https://kapi.kakao.com/v2/user/me";

    @Test
    void login() {
        // given
        mockServer.expect(requestTo(KAKAO_OAUTH_TOKEN_URL))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess("""
                {
                    "access_token": "testAccessToken",
                    "refresh_token": "testRefreshToken"
                }
                """, MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(KAKAO_PROFILE_URL))
            .andRespond(withSuccess("""
                {
                    "id": 123456789,
                    "kakao_account": {
                        "email": "test@kakao.com"
                    }
                }
                """, MediaType.APPLICATION_JSON));

        String token = "testToken";
        given(memberService.findMember(anyString())).willReturn(createMember().toDTO());
        given(jwtProvider.createAccessToken(any(MemberDTO.class))).willReturn(token);

        // when
        JwtResponse jwtResponse = kakaoLoginService.login("testCode");

        // then
        then(kakaoTokenRepository).should().save(any(KakaoToken.class));
        assertThat(jwtResponse.token()).isEqualTo(token);
    }
}
