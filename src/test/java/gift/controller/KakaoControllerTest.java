package gift.controller;

import gift.service.KakaoLoginService;
import gift.service.kakao.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class KakaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private KakaoLoginService kakaoLoginService;

    @DisplayName("[GET] 카카오톡 로그인 화면으로 Redirect")
    @Test
    void redirectLoginForm() throws Exception {
        //given
        HttpHeaders headers = new HttpHeaders();
        given(kakaoLoginService.getRedirectHeaders()).willReturn(headers);

        //when
        ResultActions result = mvc.perform(get("/kakao/login"));

        //then
        result.andExpect(status().isFound());

        then(kakaoLoginService).should().getRedirectHeaders();
    }

    @DisplayName("[GET] 로그인 후 Redirect 된 URL 처리 - 토큰을 발급하여 응답한다.")
    @Test
    void login() throws Exception {
        //given
        String code = "test_code";
        TokenResponse tokenResponse = new TokenResponse("test_access_token", "test_jwt");

        given(kakaoLoginService.processKakaoAuth(code)).willReturn(tokenResponse);

        //when
        ResultActions result = mvc.perform(get("/kakao/oauth")
                .param("code", code));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").value(tokenResponse.getAccessToken()))
                .andExpect(jsonPath("jwt").value(tokenResponse.getJwt()));

        then(kakaoLoginService).should().processKakaoAuth(code);
    }

}
