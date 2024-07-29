package gift.controller;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.error.KakaoAuthenticationException;
import gift.users.kakao.KakaoAuthApiController;
import gift.users.kakao.KakaoAuthService;
import gift.users.kakao.KakaoProperties;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KakaoAuthApiControllerTest {

    private final KakaoAuthService kakaoAuthService = mock(KakaoAuthService.class);
    private MockMvc mvc;
    private KakaoAuthApiController kakaoAuthApiController;
    @Autowired
    private KakaoProperties kakaoProperties;

    @BeforeEach
    void beforeEach() {
        kakaoAuthApiController = new KakaoAuthApiController(kakaoAuthService);
        mvc = MockMvcBuilders.standaloneSetup(kakaoAuthApiController)
            .defaultResponseCharacterEncoding(UTF_8)
            .build();
    }

    @Test
    @DisplayName("로그인 화면 리다이렉트")
    void kakaoLogin() throws Exception {
        //given
        String expectedUrl = kakaoProperties.authUrl() + "?response_type=code&client_id="
            + kakaoProperties.clientId() + "&redirect_uri=" + kakaoProperties.redirectUri();
        given(kakaoAuthService.getKakaoLoginUrl()).willReturn(expectedUrl);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.get("/api/oauth/authorize"));

        //then
        resultActions.andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(expectedUrl));
    }

    @Test
    @DisplayName("카카오 로그인시 토큰 발급")
    void kakaoCallBack() throws Exception {
        //given
        String code = "test_code";
        String accessToken = "test_access_token";
        given(kakaoAuthService.kakaoCallBack(code)).willReturn(accessToken);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.get("/api/oauth/token")
                .param("code", code)
                .contentType("application/json"));

        //then
        resultActions.andExpect(status().isOk())
            .andExpect(content().string(accessToken));
    }

    @Test
    @DisplayName("로그인 화면 리다이렉트 IOException")
    void kakaoLoginBadRedirection() throws Exception {
        //given
        String expectedUrl = kakaoProperties.authUrl() + "?response_type=code&client_id="
            + kakaoProperties.clientId() + "&redirect_uri=" + kakaoProperties.redirectUri();
        given(kakaoAuthService.getKakaoLoginUrl()).willReturn(expectedUrl);
        HttpServletResponse response = mock(HttpServletResponse.class);
        doThrow(new IOException()).when(response).sendRedirect(expectedUrl);

        //when

        //then
        assertThatThrownBy(() -> kakaoAuthApiController.kakaoLogin(response)).isInstanceOf(
            KakaoAuthenticationException.class).hasMessageContaining("카카오 로그인에 실패했습니다.");
    }
}
