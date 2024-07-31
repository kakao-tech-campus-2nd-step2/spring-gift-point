package gift.controller.oauth;

import static gift.util.constants.auth.KakaoOAuthConstants.SCOPES_FAILURE_ERROR;
import static gift.util.constants.auth.KakaoOAuthConstants.TOKEN_FAILURE_ERROR;
import static gift.util.constants.auth.KakaoOAuthConstants.UNLINK_FAILURE_ERROR;
import static gift.util.constants.auth.KakaoOAuthConstants.USERINFO_FAILURE_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.config.KakaoProperties;
import gift.dto.member.MemberAuthResponse;
import gift.dto.member.MemberOAuthResponse;
import gift.dto.oauth.KakaoScopeResponse;
import gift.dto.oauth.KakaoScopeResponse.Scope;
import gift.dto.oauth.KakaoTokenResponse;
import gift.dto.oauth.KakaoUnlinkResponse;
import gift.dto.oauth.KakaoUserResponse;
import gift.model.RegisterType;
import gift.service.MemberService;
import gift.service.oauth.KakaoOAuthService;
import gift.util.TokenValidator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(KakaoOAuthController.class)
class KakaoOAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoOAuthService kakaoOAuthService;

    @MockBean
    private TokenValidator tokenValidator;

    @MockBean
    private KakaoProperties kakaoProperties;

    @MockBean
    private MemberService memberService;

    private KakaoTokenResponse tokenResponse;
    private KakaoUnlinkResponse unlinkResponse;
    private KakaoScopeResponse scopeResponse;
    private KakaoUserResponse userResponse;
    private MemberAuthResponse memberAuthResponse;
    private MemberOAuthResponse memberOAuthResponse;

    @BeforeEach
    void setUp() {
        tokenResponse = new KakaoTokenResponse("access-token", 3600, "refresh-token", 7200);
        unlinkResponse = new KakaoUnlinkResponse(12345L);
        scopeResponse = new KakaoScopeResponse(List.of(
            new Scope("profile", "Profile Information", true),
            new Scope("email", "Email Information", false)
        ));
        userResponse = new KakaoUserResponse(12345L, "nickname", "test@example.com");
        memberAuthResponse = new MemberAuthResponse("test@example.com", "token");
        memberOAuthResponse = new MemberOAuthResponse(1L, "test@example.com", "token", RegisterType.DEFAULT);
    }

    @Test
    @DisplayName("카카오 로그인 성공")
    void testKakaoLogin() throws Exception {
        mockMvc.perform(get("/oauth/kakao"))
            .andExpect(status().isMovedPermanently());
    }

    @Test
    @DisplayName("카카오 콜백 성공")
    void testKakaoCallbackSuccess() throws Exception {
        when(kakaoOAuthService.getAccessToken(anyString())).thenReturn(tokenResponse);
        when(kakaoOAuthService.getUserInfo(anyString())).thenReturn(userResponse);
        when(kakaoOAuthService.registerOrLoginKakaoUser(any(KakaoUserResponse.class))).thenReturn(memberOAuthResponse);
        when(kakaoOAuthService.generateJwt(anyLong(), anyString())).thenReturn("token");

        mockMvc.perform(get("/oauth/kakao/callback")
                .param("code", "auth-code"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @DisplayName("카카오 콜백 실패")
    void testKakaoCallbackFailure() throws Exception {
        when(kakaoOAuthService.getAccessToken(anyString())).thenThrow(new RuntimeException(TOKEN_FAILURE_ERROR));

        mockMvc.perform(get("/oauth/kakao/callback")
                .param("code", "auth-code"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error").value(TOKEN_FAILURE_ERROR));
    }

    @Test
    @DisplayName("카카오 사용자 연결 해제 성공")
    void testUnlinkUserSuccess() throws Exception {
        when(kakaoOAuthService.unlinkUser(anyLong())).thenReturn(unlinkResponse);

        mockMvc.perform(delete("/oauth/kakao/unlink")
                .requestAttr("memberId", 12345L))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("카카오 사용자 연결 해제 실패")
    void testUnlinkUserFailure() throws Exception {
        when(kakaoOAuthService.unlinkUser(anyLong())).thenThrow(new RuntimeException(UNLINK_FAILURE_ERROR));

        mockMvc.perform(delete("/oauth/kakao/unlink")
                .requestAttr("memberId", 12345L))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error").value(UNLINK_FAILURE_ERROR));
    }

    @Test
    @DisplayName("카카오 사용자 동의 내역 조회 성공")
    void testGetUserScopesSuccess() throws Exception {
        when(kakaoOAuthService.getUserScopes(anyLong())).thenReturn(scopeResponse);

        mockMvc.perform(get("/oauth/kakao/scopes")
                .requestAttr("memberId", 12345L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.scopes[0].id").value("profile"))
            .andExpect(jsonPath("$.scopes[0].agreed").value(true))
            .andExpect(jsonPath("$.scopes[1].id").value("email"))
            .andExpect(jsonPath("$.scopes[1].agreed").value(false));
    }

    @Test
    @DisplayName("카카오 사용자 동의 내역 조회 실패")
    void testGetUserScopesFailure() throws Exception {
        when(kakaoOAuthService.getUserScopes(anyLong())).thenThrow(new RuntimeException(SCOPES_FAILURE_ERROR));

        mockMvc.perform(get("/oauth/kakao/scopes")
                .requestAttr("memberId", 12345L))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error").value(SCOPES_FAILURE_ERROR));
    }

    @Test
    @DisplayName("카카오 사용자 정보 조회 성공")
    void testGetUserInfoSuccess() throws Exception {
        when(kakaoOAuthService.getUserInfo(anyLong())).thenReturn(userResponse);

        mockMvc.perform(get("/oauth/kakao/userinfo")
                .requestAttr("memberId", 12345L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(12345L))
            .andExpect(jsonPath("$.nickname").value("nickname"))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("카카오 사용자 정보 조회 실패")
    void testGetUserInfoFailure() throws Exception {
        when(kakaoOAuthService.getUserInfo(anyLong())).thenThrow(new RuntimeException(USERINFO_FAILURE_ERROR));

        mockMvc.perform(get("/oauth/kakao/userinfo")
                .requestAttr("memberId", 12345L))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error").value(USERINFO_FAILURE_ERROR));
    }
}
