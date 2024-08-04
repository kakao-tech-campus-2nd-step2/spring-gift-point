package gift.web.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.authentication.token.JwtProvider;
import gift.config.RestDocsConfiguration;
import gift.domain.Member;
import gift.domain.Member.Builder;
import gift.domain.vo.Email;
import gift.mock.MockLoginMemberArgumentResolver;
import gift.service.OrderService;
import gift.web.dto.request.order.CreateOrderRequest;
import gift.web.dto.response.order.OrderResponse;
import java.time.LocalDateTime;
import java.util.List;
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
class OrderApiControllerTest {

    private MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private OrderService orderService;

    private String accessToken;

    private static final String BASE_URL = "/api/orders";

    @BeforeEach
    void setUp(
        final RestDocumentationContextProvider provider
    ) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new OrderApiController(orderService))
            .setCustomArgumentResolvers(new MockLoginMemberArgumentResolver(), new PageableHandlerMethodArgumentResolver())
            .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
            .alwaysDo(restDocs)
            .build();

        Member member = new Builder().id(1L).name("회원01").email(Email.from("member01@gmail.com")).build();
        accessToken = jwtProvider.generateToken(member).getValue();
    }

    @Test
    @DisplayName("상품 주문")
    void orderProduct() throws Exception {

        CreateOrderRequest request = new CreateOrderRequest(1L, 1, "message");
        String content = objectMapper.writeValueAsString(request);

        given(orderService.createOrder(any(String.class), any(Long.class), any()))
            .willReturn(new OrderResponse(1L, 1L, 10, "message", LocalDateTime.now()));

        mockMvc
            .perform(
                post(BASE_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("optionId").type(JsonFieldType.NUMBER).description("상품 옵션 ID"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("주문 수량"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("주문 ID"),
                        fieldWithPath("optionId").type(JsonFieldType.NUMBER).description("상품 옵션 ID"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("주문 수량"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                        fieldWithPath("orderDateTime").type(JsonFieldType.ARRAY).description("주문 일시")
                    )
                )
            );
    }

    @Test
    @DisplayName("주문 목록 조회")
    void readOrders() throws Exception {

        given(orderService.readOrders(any()))
            .willReturn(
                List.of(
                    new OrderResponse(1L, 1L, 10, "message", LocalDateTime.now()),
                    new OrderResponse(2L, 2L, 20, "message", LocalDateTime.now())
                )
            );

        mockMvc
            .perform(
                get(BASE_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("주문 ID"),
                        fieldWithPath("[].optionId").type(JsonFieldType.NUMBER).description("상품 옵션 ID"),
                        fieldWithPath("[].quantity").type(JsonFieldType.NUMBER).description("주문 수량"),
                        fieldWithPath("[].message").type(JsonFieldType.STRING).description("메시지"),
                        fieldWithPath("[].orderDateTime").type(JsonFieldType.ARRAY).description("주문 일시")
                    )
                )
            );
    }
}