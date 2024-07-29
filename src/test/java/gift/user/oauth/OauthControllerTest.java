package gift.user.oauth;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.controller.OauthController;
import gift.dto.oauth.KakaoTokenResponse;
import gift.service.JwtUserService;
import gift.service.KakaoLoginService;
import gift.util.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OauthController.class)
@EntityScan(basePackages = "gift")
@MockBean(JpaMetamodelMappingContext.class)
public class OauthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoLoginService kakaoLoginService;

    @MockBean
    private JwtUserService jwtUserService;

    @MockBean
    private JwtService jwtService;

    private KakaoTokenResponse mockToken;

    @BeforeEach
    public void setUp() {
        mockToken = new KakaoTokenResponse(
                "bearer",
                "abcd1234",
                3600L,
                "refresh1234",
                1209600L
        );
    }

    @Test
    public void testKakaoLoginRedirect() throws Exception {
        when(kakaoLoginService.buildAuthorizeUrl()).thenReturn("https://kauth.kakao.com/oauth/authorize?client_id=123");

        mockMvc.perform(get("/oauth/login/kakao"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://kauth.kakao.com/oauth/authorize?client_id=123"));
    }

    @Test
    public void testKakaoCallbackSuccess() throws Exception {
        when(kakaoLoginService.getAccessToken(anyString())).thenReturn(mockToken);
        when(kakaoLoginService.getUserInfo(mockToken.accessToken())).thenReturn("email");
        when(jwtUserService.loginOauth("email", mockToken.accessToken())).thenReturn("jwt_token");

        mockMvc.perform(get("/oauth/kakao").param("code", "codecode"))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "jwt_token"))
                .andExpect(content().string("로그인 성공"));
    }

    @Test
    public void testKakaoCallbackFailure() throws Exception {
        when(kakaoLoginService.getAccessToken(anyString())).thenReturn(mockToken);
        when(kakaoLoginService.getUserInfo(mockToken.accessToken())).thenReturn("email");
        when(jwtUserService.loginOauth("email", mockToken.accessToken())).thenReturn(null);

        mockMvc.perform(get("/oauth/kakao").param("code", "codecode"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("회원가입이 필요합니다."));
    }
}
