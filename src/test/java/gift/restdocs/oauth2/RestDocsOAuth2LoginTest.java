package gift.restdocs.oauth2;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.auth.JwtService;
import gift.auth.JwtTokenProvider;
import gift.auth.OAuthService;
import gift.config.LoginWebConfig;
import gift.controller.oauth2.OAuth2LoginController;
import gift.response.oauth2.OAuth2TokenResponse;
import gift.restdocs.AbstractRestDocsTest;
import gift.service.MemberService;
import gift.service.OAuth2LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.util.ReflectionTestUtils;

@WebMvcTest(value = OAuth2LoginController.class,
    excludeFilters = {@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LoginWebConfig.class)})
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class RestDocsOAuth2LoginTest extends AbstractRestDocsTest {

    @MockBean
    private OAuth2LoginService oAuth2LoginService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private OAuthService oAuthService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private String token = "{ACCESS_TOKEN}";


    @Test
    void getToken() throws Exception {
        //given
        ReflectionTestUtils.setField(jwtService, "jwtTokenProvider", jwtTokenProvider);
        String authorizationCode = "KAKAO_AUTHORIZATION_CODE";

        OAuth2TokenResponse oAuth2TokenResponse = new OAuth2TokenResponse(token, "bearer", null,
            21599, null, "talk_message");

        doNothing().when(oAuth2LoginService).checkRedirectUriParams(any(HttpServletRequest.class));
        given(oAuth2LoginService.getToken(any(String.class)))
            .willReturn(oAuth2TokenResponse);
        doCallRealMethod().when(jwtService).addOAuthTokenInCookie(any(String.class), any(
            HttpServletResponse.class));

        //when //then
        mockMvc.perform(get("/api/oauth2/kakao?code=" + authorizationCode))
            .andExpect(status().isOk())
            .andExpect(cookie().value("access_token", token))
            .andDo(MockMvcRestDocumentation.document("rest-docs-o-auth2-login-test/get-token",
                queryParameters(
                    parameterWithName("code").description("카카오 서버로부터 전달받은 인가 코드")
                )));
    }
}
