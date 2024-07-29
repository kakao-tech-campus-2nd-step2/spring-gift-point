package gift.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.security.LoginMemberArgumentResolver;
import gift.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(KakaoLoginController.class)
@TestPropertySource(properties = {"kakao.client-id=", "kakao.redirect-url="})
@AutoConfigureRestDocs(outputDir = "target/snippets")
class KakaoLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HttpSession session;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-url}")
    private String redirectUrl;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .alwaysDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .build();
    }

    @Test
    void kakaoLoginPageWithoutAuthCodeTest() throws Exception {
        // when & then
        mockMvc.perform(get("/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(view().name("kakaologin"));
    }

    @Test
    void kakaoGetTokenTest() throws Exception {
        // given
        var url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + clientId);
        url.append("&redirect_uri=" + redirectUrl);
        url.append("&response_type=code");
        given(memberService.getAuthentificationCode()).willReturn(url);

        // when & then
        mockMvc.perform(get("/kakao/authcode").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", url.toString()));
    }

    @Test
    void kakaoLoginPageWithAuthCodeTest() throws Exception {
        // given
        String testAuthCode = "TestAuthCode";

        // when & then
        mockMvc.perform(
                get("/").contentType(MediaType.APPLICATION_JSON).param("code", testAuthCode))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/kakao/login"))
            .andExpect(result -> {
                HttpSession session = result.getRequest().getSession();
                assertNotNull(session);
                assertEquals(testAuthCode, session.getAttribute("kakao_auth_code"));
            });
    }
}