package gift.ControllerTest;

import gift.Annotation.LoginMemberArgumentResolver;
import gift.Controller.KakaoOAuthController;
import gift.Service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KakaoOAuthController.class)
public class KakaoOAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private MemberService memberService;

    @MockBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthorize() throws Exception {
        mockMvc.perform(get("/oauth/authorize"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testCallBackSuccess() throws Exception {
        String code = "unique-success-code";

        // 액세스 토큰 요청을 모킹
        given(restTemplate.exchange(
                eq("https://kauth.kakao.com/oauth/token"),
                eq(HttpMethod.POST),
                any(),
                eq(String.class)))
                .willReturn(ResponseEntity.ok("{\"token_type\": \"bearer\", \"access_token\": \"valid-access-token\"}"));

        // 사용자 정보 요청을 모킹
        given(restTemplate.exchange(
                eq("https://kapi.kakao.com/v2/user/me"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class)))
                .willReturn(ResponseEntity.ok("{\"id\": 12345}"));

        // MockMvc 요청 빌더 생성
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/auth/kakao/callback")
                .param("code", code);

        // MockMvc 수행 및 검증
        mockMvc.perform(requestBuilder)
                .andExpect(status().isSeeOther())
                .andExpect(header().string("Location", "/products"))
                .andExpect(cookie().exists("accessToken"))
                .andExpect(request().sessionAttribute("kakaoId", 12345L))
                .andExpect(content().string(containsString("Successfully logged in")));
    }

    @Test
    void testCallBackUsedCode() throws Exception {
        String code = "unique-used-code";

        // 액세스 토큰 요청을 모킹
        given(restTemplate.exchange(
                eq("https://kauth.kakao.com/oauth/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)))
                .willReturn(ResponseEntity.badRequest().body("{\"access_token\": \"invalid_grant\"}"));

        // MockMvc 요청 빌더 생성
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/auth/kakao/callback")
                .param("code", code);

        // MockMvc 수행 및 검증
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Failed to retrieve access token")));
    }

    @Test
    void testCallBackCommunicationFailure() throws Exception {
        String code = "unique-failure-code";

        // 액세스 토큰 요청을 모킹
        given(restTemplate.exchange(
                eq("https://kauth.kakao.com/oauth/token"),
                eq(HttpMethod.POST),
                any(),
                eq(String.class)))
                .willReturn(ResponseEntity.status(500).body(""));

        // MockMvc 요청 빌더 생성
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/auth/kakao/callback")
                .param("code", code);

        // MockMvc 수행 및 검증
        mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError());
    }
}
