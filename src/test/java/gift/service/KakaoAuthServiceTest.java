package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import gift.error.KakaoAuthenticationException;
import gift.token.TokenService;
import gift.users.kakao.KakaoAuthService;
import gift.users.kakao.KakaoProperties;
import gift.users.kakao.KakaoTokenDTO;
import gift.users.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KakaoAuthServiceTest {

    private MockRestServiceServer server;
    private KakaoAuthService kakaoAuthService;
    private UserService userService = mock(UserService.class);
    private RestClient.Builder restClientBuilder;
    private TokenService tokenService = mock(TokenService.class);
    @Autowired
    private KakaoProperties kakaoProperties;

    @BeforeEach
    void beforeEach() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        restClientBuilder = RestClient.builder()
            .requestFactory(factory)
            .defaultHeaders(headers -> {
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            });
        server = MockRestServiceServer.bindTo(restClientBuilder).build();
        kakaoAuthService = new KakaoAuthService(kakaoProperties, restClientBuilder,
            userService, tokenService);
    }

    @Test
    @DisplayName("카카오 로그인 성공")
    void kakaoCallBackSuccess() {
        //given
        String code = "test_code";
        String tokenResponse = "{\"access_token\":\"test_access_token\"}";
        String userResponse = "{\"id\":1}";

        server.expect(requestTo(kakaoProperties.tokenUrl()))
            .andExpect(method(POST))
            .andRespond(withSuccess(tokenResponse, MediaType.APPLICATION_JSON));

        server.expect(requestTo(kakaoProperties.userUrl()))
            .andExpect(method(POST))
            .andRespond(withSuccess(userResponse, MediaType.APPLICATION_JSON));

        given(userService.findBySnsIdAndSnsAndRegisterIfNotExists(anyString(), anyString())).willReturn(1L);
        given(userService.loginGiveJwt(anyString())).willReturn("user_token");
        doNothing().when(tokenService).saveToken(anyLong(), any(KakaoTokenDTO.class), anyString());

        //when
        String result = kakaoAuthService.kakaoCallBack(code);

        //then
        assertThat(result).isEqualTo("user_token");
    }

    @Test
    @DisplayName("카카오 로그인 실패 BAD_REQUEST")
    void kakaoCallBackFailedWhenBadRequest() {
        //given
        String code = "test_code";

        server.expect(requestTo(kakaoProperties.tokenUrl()))
            .andExpect(method(POST))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        //when

        //then
        assertThatThrownBy(() -> kakaoAuthService.kakaoCallBack(code))
            .isInstanceOf(KakaoAuthenticationException.class)
            .hasMessageContaining("카카오 토큰 값을 서버에서 가져오는 데에 실패했습니다.");
    }

    @Test
    @DisplayName("카카오 로그인 실패 DTO에 null 값을 전달 받음")
    void kakaoCallBackFailedWhenBodyIsNull() {
        //given
        String code = "test_code";

        server.expect(requestTo(kakaoProperties.tokenUrl()))
            .andExpect(method(POST))
            .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        //when

        //then
        assertThatThrownBy(() -> kakaoAuthService.kakaoCallBack(code))
            .isInstanceOf(KakaoAuthenticationException.class)
            .hasMessageContaining("카카오 토큰 값이 비어있습니다.");
    }
}
