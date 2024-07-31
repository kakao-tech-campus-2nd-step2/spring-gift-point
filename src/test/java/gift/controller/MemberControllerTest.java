package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.ApiResponse;
import gift.exception.IllegalEmailException;
import gift.member.controller.MemberController;
import gift.member.dto.MemberRequest;
import gift.member.model.Member;
import gift.member.service.MemberService;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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
    void setUp(RestDocumentationContextProvider restDocumentation) throws IllegalEmailException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .alwaysDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .build();

        member = new Member("test@email.com", "password");
    }

    @Test
    void registerMemberSuccess() throws Exception, IllegalEmailException {
        // Given
        String token = "generatedToken";
        given(memberService.registerMember(any(MemberRequest.class))).willReturn(
            Optional.of(token));

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member))).
            andExpect(status().isOk()).andDo(print())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("회원 가입 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("members/register-success",
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("id").description("회원 ID").optional(),
                    fieldWithPath("email").description("이메일").optional(),
                    fieldWithPath("kakaoId").description("카카오 ID").optional(),
                    fieldWithPath("wishList").description("위시리스트").optional(),
                    fieldWithPath("orders").description("주문").optional()
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("호출 내용"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data").description("응답 데이터 배열"),
                    fieldWithPath("data[].email").description("이메일"),
                    fieldWithPath("data[].password").description("비밀번호"),
                    fieldWithPath("data[].token").description("로그인 토큰")
                )));
    }

    @Test
    void registerMemberFail() throws Exception, IllegalEmailException {
        // Given
        when(memberService.registerMember(any(MemberRequest.class))).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
            .andExpect(status().isBadRequest()).andDo(print())
            .andExpect(jsonPath("$.result").value("ERROR"))
            .andExpect(jsonPath("$.message").value("회원 가입 실패"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andDo(document("members/register-fail",
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("id").description("회원 ID").optional(),
                    fieldWithPath("email").description("이메일").optional(),
                    fieldWithPath("kakaoId").description("카카오 ID").optional(),
                    fieldWithPath("wishList").description("위시리스트").optional(),
                    fieldWithPath("orders").description("주문").optional()
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("호출 내용"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data[].error").description("에러 내용")
                )));
    }

    @Test
    void loginSuccess() throws Exception, IllegalEmailException {
        // Given
        String token = "loginToken";
        when(memberService.login(member.getEmail(), member.getPassword())).thenReturn(
            Optional.of(token));

        // When & Then
        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
            .andExpect(status().isOk()).andDo(print())
            .andExpect(jsonPath("$.message").value("로그인 성공"))
            .andDo(document("members/login-success",
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("id").description("회원 ID").optional(),
                    fieldWithPath("email").description("이메일").optional(),
                    fieldWithPath("kakaoId").description("카카오 ID").optional(),
                    fieldWithPath("wishList").description("위시리스트").optional(),
                    fieldWithPath("orders").description("주문").optional()
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("호출 내용"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data").description("응답 데이터 배열"),
                    fieldWithPath("data[].email").description("이메일"),
                    fieldWithPath("data[].password").description("비밀번호"),
                    fieldWithPath("data[].token").description("로그인 토큰")
                )));
    }

    @Test
    void loginFail() throws Exception, IllegalEmailException {
        // Given
        when(memberService.login(member.getEmail(), member.getPassword())).thenReturn(
            Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
            .andExpect(status().isForbidden()).andDo(print())
            .andDo(document("members/login-fail", responseFields(
                fieldWithPath("result").description("API 호출 결과"),
                fieldWithPath("message").description("호출 내용"),
                fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                fieldWithPath("data[0].Error").description("에러 내용")
            )));
    }
}