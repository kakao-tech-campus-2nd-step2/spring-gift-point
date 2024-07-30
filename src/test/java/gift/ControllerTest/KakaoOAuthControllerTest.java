package gift.ControllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class KakaoOAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
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
        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().string(containsString("code=" + code)))
                .andRespond(withSuccess("{\"token_type\": \"bearer\", \"access_token\": \"valid-access-token\"}", MediaType.APPLICATION_JSON));

        // 사용자 정보 요청을 모킹
        mockServer.expect(requestTo("https://kapi.kakao.com/v2/user/me"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"id\": 12345}", MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/auth/kakao/callback").param("code", code))
                .andExpect(status().isSeeOther())
                .andExpect(header().string("Location", "/products"))
                .andExpect(cookie().exists("accessToken"))
                .andExpect(request().sessionAttribute("kakaoId", 12345L))
                .andExpect(content().string(containsString("Successfully logged in")));

        mockServer.verify();
    }

    @Test
    void testCallBackUsedCode() throws Exception {
        String code = "unique-used-code";

        // 액세스 토큰 요청을 모킹
        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().string(containsString("code=" + code)))
                .andRespond(MockRestResponseCreators.withBadRequest().body("{\"error\":\"invalid_grant\",\"error_description\":\"Authorization code is already used.\"}"));

        mockMvc.perform(get("/auth/kakao/callback").param("code", code))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Authorization code is already used.")));

        mockServer.verify();
    }

    @Test
    void testCallBackCommunicationFailure() throws Exception {
        String code = "unique-failure-code";

        // 액세스 토큰 요청을 모킹
        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().string(containsString("code=" + code)))
                .andRespond(MockRestResponseCreators.withServerError());

        mockMvc.perform(get("/auth/kakao/callback").param("code", code))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Internal server error occurred")));

        mockServer.verify();
    }
}
