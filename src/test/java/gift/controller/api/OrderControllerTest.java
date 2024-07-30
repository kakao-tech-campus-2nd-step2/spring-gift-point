package gift.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.interceptor.AuthInterceptor;
import gift.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class OrderControllerTest {

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;
    @MockBean
    private AuthInterceptor authInterceptor;
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation).snippets().withDefaults(httpRequest(), httpResponse(), requestBody(), responseBody()))
                .build();
    }


    @Test
    void order() throws Exception {
        //Given
        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        OrderRequest orderRequest = new OrderRequest(1L, 10, "잘 부탁드립니다.");
        OrderResponse orderResponse = new OrderResponse(1L, 1L, 10, LocalDateTime.now(), "잘 부탁드립니다.");

        when(orderService.processOrder(any(), any())).thenReturn(orderResponse);

        //When
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validTokenValue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(orderResponse))
                )
                .andDo(document("order-example",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Authorization: Bearer ${ACCESS_TOKEN} +\n인증방식, 액세스 토큰으로 인증요청").optional()
                                ),
                                requestFields(
                                        fieldWithPath("optionId").description("주문할 상품의 옵션 ID").type(JsonFieldType.NUMBER),
                                        fieldWithPath("quantity").description("주문할 상품의 수량").type(JsonFieldType.NUMBER),
                                        fieldWithPath("message").description("전달할 배송 메시지. 200자까지 입력 가능").type(JsonFieldType.STRING).optional()
                                ),
                                responseFields(
                                        fieldWithPath("id").description("주문 번호").type(JsonFieldType.NUMBER),
                                        fieldWithPath("optionId").description("주문된 상품의 옵션 ID").type(JsonFieldType.NUMBER),
                                        fieldWithPath("quantity").description("주문된 수량").type(JsonFieldType.NUMBER),
                                        fieldWithPath("orderDateTime").description("주문 일시").type(JsonFieldType.STRING),
                                        fieldWithPath("message").description("전달된 배송 메시지").type(JsonFieldType.STRING)
                                )
                        )
                );
    }
}
