package gift.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.MemberRequest;
import gift.dto.response.JwtResponse;
import gift.service.MemberService;
import gift.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})

@DisplayName("멤버 컨트롤러 단위테스트")
class MemberControllerTest {

    private static final String REGISTER_URL = "/members/register";
    private static final String LOGIN_URL = "/members/login";
    @MockBean
    JpaMetamodelMappingContext jpaMetamodelMappingContext;
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberService memberService;
    @MockBean
    private TokenService tokenService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation).snippets().withDefaults(httpRequest(), httpResponse(), requestBody(), responseBody()))
                .build();
    }

    @Test
    @DisplayName("회원가입")
    void registerMember() throws Exception {
        //Given
        MemberRequest registerRequest = new MemberRequest("test@test.com", "1234");
        JwtResponse jwtResponse = new JwtResponse("{access_token}");

        when(memberService.register(registerRequest)).thenReturn(1L);
        when(tokenService.generateJwt(1L)).thenReturn(jwtResponse);

        //When
        mockMvc.perform(RestDocumentationRequestBuilders
                        .post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                //Then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("token").exists()
                )
                .andDo(document("member-register",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),

                                requestFields(
                                        fieldWithPath("email").description("사용할 이메일").type(JsonFieldType.STRING),
                                        fieldWithPath("password").description("사용할 비밀번호").type(JsonFieldType.STRING)
                                ),
                                responseFields(
                                        fieldWithPath("token").description("액세스 토큰").type(JsonFieldType.STRING)
                                )
                        )
                );
    }

    @Test
    @DisplayName("로그인")
    void loginMember() throws Exception {
        //Given
        MemberRequest loginRequest = new MemberRequest("test@test.com", "1234");
        JwtResponse jwtResponse = new JwtResponse("{access_token}");

        when(memberService.login(loginRequest)).thenReturn(1L);
        when(tokenService.generateJwt(1L)).thenReturn(jwtResponse);

        //When
        mockMvc.perform(RestDocumentationRequestBuilders
                        .post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                //Then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("token").exists()
                )
                .andDo(document("member-login",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Authorization: Bearer ${ACCESS_TOKEN}\n로그인으로 받게되는 토큰").optional()
                        ),
                        requestFields(
                                fieldWithPath("email").description("로그인에 필요한 이메일").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("로그인에 필요한 비밀번호").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("token").description("액세스 토큰").type(JsonFieldType.STRING)
                        )
                ));
    }
}
