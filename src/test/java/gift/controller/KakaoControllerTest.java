package gift.controller;

import gift.service.KakaoAuthService;
import gift.service.KakaoMessageService;
import gift.service.OrderService;
import gift.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = KakaoController.class)
@ActiveProfiles("test")
public class KakaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoAuthService kakaoAuthService;

    @MockBean
    private KakaoMessageService kakaoMessageService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private TokenService tokenService;

    @Test
    @DisplayName("카카오 로그인 테스트")
    void kakaoLogin() throws Exception {
        String clientId = "test-client-id";
        String redirectUri = "http://localhost:8080/callback";
        String authUrl = "https://kauth.kakao.com/oauth/authorize";
        String expectedUrl = authUrl + "?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;

        when(kakaoAuthService.getClientId()).thenReturn(clientId);
        when(kakaoAuthService.getRedirectUri()).thenReturn(redirectUri);

        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedUrl));
    }

    @Test
    @DisplayName("카카오 콜백 테스트")
    void callback() throws Exception {
        String code = "test-code";
        String accessToken = "test-access-token";

        when(kakaoAuthService.getAccessToken(anyString())).thenReturn(accessToken);

        mockMvc.perform(get("/callback").param("code", code))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }
}
