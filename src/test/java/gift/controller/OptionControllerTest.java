package gift.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.member.model.Member;
import gift.member.service.MemberService;
import gift.option.controller.OptionController;
import gift.option.model.Option;
import gift.option.service.OptionService;
import gift.security.LoginMemberArgumentResolver;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(OptionController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class OptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OptionService optionService;

    @MockBean
    private MemberService memberService;  // MemberService 모의 객체 추가

    @MockBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;  // LoginMemberArgumentResolver 모의 객체 추가

    @Autowired
    private ObjectMapper objectMapper;
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

        // LoginMemberArgumentResolver 설정
        given(loginMemberArgumentResolver.supportsParameter(any(MethodParameter.class)))
            .willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
            .willReturn(new Member());  // 필요에 따라 Member 객체 설정
    }

    @Test
    void getAllOptionsByProductId() throws Exception {
        Long productId = 1L;
        List<Option> options = Arrays.asList(
            new Option("Option 1", 100),
            new Option("Option 2", 200)
        );
        options.get(0).setId(1L);
        options.get(1).setId(2L);

        given(optionService.findOptionsByProductId(productId)).willReturn(options);

        mockMvc.perform(get("/api/products/{productId}/options", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andDo(print())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("옵션 조회 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.data", hasSize(2)))
            .andDo(document("get-all-options-by-product-id",
                pathParameters(
                    parameterWithName("productId").description("제품 ID")
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("메시지"),
                    fieldWithPath("httpStatus").description("HTTP 상태"),
                    fieldWithPath("data").description("옵션 목록"),
                    fieldWithPath("data[].id").description("옵션 ID"),
                    fieldWithPath("data[].name").description("옵션 이름"),
                    fieldWithPath("data[].quantity").description("옵션 수량")
//                    fieldWithPath("data[].optionResponseList").description("옵션 응답 리스트"),
//                    fieldWithPath("data[].optionResponseList[].id").description("옵션 응답 ID"),
//                    fieldWithPath("data[].optionResponseList[].name").description("옵션 응답 이름"),
//                    fieldWithPath("data[].optionResponseList[].productList").description("제품 목록").optional(),
//                    fieldWithPath("data[].optionResponseList[].quantity").description("옵션 응답 수량"),
//                    fieldWithPath("data[].optionResponseMap").description("옵션 응답 맵"),
//                    fieldWithPath("data[].optionResponseMap.id").description("옵션 맵 ID"),
//                    fieldWithPath("data[].optionResponseMap.name").description("옵션 맵 이름"),
//                    fieldWithPath("data[].optionResponseMap.productList").description("제품 목록").optional(),
//                    fieldWithPath("data[].optionResponseMap.quantity").description("옵션 맵 수량")

                )
            ));

    }
}