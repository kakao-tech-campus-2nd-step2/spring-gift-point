package gift.controller.auth;

import gift.repository.token.TokenRepository;
import gift.service.KakaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mvc;

    @MockBean
    private KakaoService kakaoService;

    @MockBean
    TokenRepository tokenRepository;

    @Test
    @DisplayName("카카오 OAuth 로그인 요청 테스트")
    void 카카오_로그인_요청_테스트() throws Exception {
        //given
        String expectedParam = "scope=talk_message&response_type=code&redirect_uri=http://localhost:8080&client_id=REST_API_KEY";
        given(kakaoService.makeKakaOauthParameter())
                .willReturn(new StringBuilder(expectedParam));
        //excepte
        mvc.perform(get("/login/oauth/kakao"))
                .andExpect(view().name("redirect:https://kauth.kakao.com/oauth/authorize?"+expectedParam))
                .andExpect(redirectedUrl("https://kauth.kakao.com/oauth/authorize?"+expectedParam))
                .andDo(print());
    }

}