package gift.domain.member.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.auth.jwt.JwtToken;
import gift.domain.member.service.KakaoLoginService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class OauthLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoLoginService kakaoLoginService;


    @Test
    @DisplayName("code를 통해 카카오 로그인 후 토큰을 반환한다.")
    void login() throws Exception {
        // given
        JwtToken expected = new JwtToken("testToken");
        given(kakaoLoginService.login(anyString())).willReturn(expected);

        // when & then
        mockMvc.perform(get("/oauth/login/kakao/callback?code=testCode"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token", Is.is("testToken")));
    }
}