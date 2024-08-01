package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.ApiResponse;
import gift.model.Member;
import gift.service.MemberService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private Member member;
    private ApiResponse apiResponse;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .alwaysDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .build();

        member = new Member("test@email.com", "password");
    }

    @Test
    void registerMemberSuccess() throws Exception {
        // Given
        String token = "generatedToken";
        given(memberService.registerMember(any(Member.class))).willReturn(Optional.of(token));

        // When & Then
        mockMvc.perform(post("/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member))).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Member Register success"))
            .andDo(document("products/register-success", responseFields(
                fieldWithPath("result").description("API 호출 결과"),
                fieldWithPath("message").description("호출 내용"),
                fieldWithPath("httpStatus").description("HTTP 응답 상태")
            )));
    }

    @Test
    void registerMemberFail() throws Exception {
        // Given
        when(memberService.registerMember(any(Member.class))).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message").value("Registration Failed, 올바른 이메일 형식이 아닙니다."))
            .andDo(document("products/register-fail", responseFields(
                fieldWithPath("result").description("API 호출 결과"),
                fieldWithPath("message").description("호출 내용"),
                fieldWithPath("httpStatus").description("HTTP 응답 상태")
            )));
    }

    @Test
    void loginSuccess() throws Exception {
        // Given
        String token = "loginToken";
        when(memberService.login(member.getEmail(), member.getPassword())).thenReturn(
            Optional.of(token));

        // When & Then
        mockMvc.perform(post("/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Request Success. 정상 로그인 되었습니다"))
            .andDo(document("products/login-success", responseFields(
                fieldWithPath("result").description("API 호출 결과"),
                fieldWithPath("message").description("호출 내용"),
                fieldWithPath("httpStatus").description("HTTP 응답 상태")
            )));
    }

    @Test
    void loginFail() throws Exception {
        // Given
        when(memberService.login(member.getEmail(), member.getPassword())).thenReturn(
            Optional.empty());

        // When & Then
        mockMvc.perform(post("/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
            .andExpect(status().isForbidden())
            .andDo(document("products/login-fail", responseFields(
                fieldWithPath("result").description("API 호출 결과"),
                fieldWithPath("message").description("호출 내용"),
                fieldWithPath("httpStatus").description("HTTP 응답 상태")
            )));
    }
}