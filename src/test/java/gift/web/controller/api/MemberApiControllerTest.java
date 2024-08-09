package gift.web.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.authentication.token.JwtProvider;
import gift.config.RestDocsConfiguration;
import gift.domain.Member;
import gift.domain.Member.Builder;
import gift.domain.vo.Email;
import gift.mock.MockLoginMemberArgumentResolver;
import gift.service.MemberService;
import gift.web.dto.request.LoginRequest;
import gift.web.dto.request.member.CreateMemberRequest;
import gift.web.dto.response.LoginResponse;
import gift.web.dto.response.member.CreateMemberResponse;
import gift.web.dto.response.member.PointResponse;
import gift.web.dto.response.member.ReadMemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ActiveProfiles("test")
@SpringBootTest
@Import(RestDocsConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
class MemberApiControllerTest {

    private MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private MemberService memberService;

    private String accessToken;

    private static final String BASE_URL = "/api/members";

    @BeforeEach
    void setUp(
        final RestDocumentationContextProvider provider
    ) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new MemberApiController(memberService))
            .setCustomArgumentResolvers(new MockLoginMemberArgumentResolver(), new PageableHandlerMethodArgumentResolver())
            .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
            .alwaysDo(restDocs)
            .build();

        Member member = new Builder().id(1L).name("회원01").email(Email.from("member01@gmail.com"))
            .build();
        accessToken = jwtProvider.generateToken(member).getValue();
    }

    @Test
    @DisplayName("회원 가입")
    void createMember() throws Exception {
        CreateMemberRequest request = new CreateMemberRequest("member01@gmail.com", "password01",
            "member01");

        String content = objectMapper.writeValueAsString(request);

        given(memberService.createMember(any()))
            .willReturn(new CreateMemberResponse(1L, "member01@gmail.com", "member01", accessToken));

        mockMvc
            .perform(
                post(BASE_URL + "/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isCreated())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 식별자"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("token").type(JsonFieldType.STRING).description("Bearer Token")
                    )
                )
            );
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        LoginRequest request = new LoginRequest("member01@gmail.com", "password01");
        String content = objectMapper.writeValueAsString(request);

        given(memberService.login(any()))
            .willReturn(new LoginResponse(accessToken));

        mockMvc
            .perform(
                post(BASE_URL + "/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Bearer Token")
                    )
                )
            );
    }

    @Test
    @DisplayName("회원 조회")
    void readMember() throws Exception {
        given(memberService.readMember(any(Long.class)))
            .willReturn(new ReadMemberResponse(1L, "member01@gmail.com", "password01", "member01"));

        mockMvc
            .perform(
                get(BASE_URL + "/{memberId}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 식별자"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                    )
                )
            );
    }

    @Test
    @DisplayName("포인트 조회")
    void readPoint() throws Exception {
        given(memberService.readPoint(any(Long.class)))
            .willReturn(new PointResponse(100));

        mockMvc
            .perform(
                get(BASE_URL + "/points")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    responseFields(
                        fieldWithPath("point").type(JsonFieldType.NUMBER).description("포인트")
                    )
                )
            );
    }

}