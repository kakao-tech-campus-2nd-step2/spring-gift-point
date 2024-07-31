package gift.controller;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;


import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.IllegalEmailException;
import gift.member.model.Member;
import gift.member.service.MemberService;
import gift.order.controller.OrderController;
import gift.order.dto.OrderRequest;
import gift.order.model.Order;
import gift.order.service.OrderService;
import gift.product.model.Product;
import gift.security.LoginMemberArgumentResolver;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(OrderController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${secret.key}")
    private String secretKey;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .build();
    }

    @Test
    void addOrder() throws Exception, IllegalEmailException {
        // Given
        Member member = new Member("test@example.com", "password");
        member.setId(1L);
        OrderRequest orderRequest = new OrderRequest(1L, 1L, 1, "Test message");
        Order order = new Order(1L, orderRequest.getOptionId(), orderRequest.getQuantity(), orderRequest.getMessage(),member);

        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);
        given(orderService.addOrder(any(Member.class), any(OrderRequest.class))).willReturn(order);

        String jwt = Jwts.builder()
            .setSubject(member.getId().toString())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();

        // When & Then
        mockMvc.perform(post("/api/orders")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("주문 추가 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("add-order",
                requestHeaders(
                    headerWithName("Authorization").description("Bearer token")
                ),
                requestFields(
                    fieldWithPath("optionId").description("주문 옵션 ID"),
                    fieldWithPath("productId").description("상품 ID"),
                    fieldWithPath("quantity").description("주문 수량"),
                    fieldWithPath("message").description("주문 메시지")
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("httpStatus").description("HTTP 상태"),
                    fieldWithPath("data").description("주문 데이터"),
                    fieldWithPath("data.id").description("주문 데이터 ID"),
                    fieldWithPath("data.localDateTime").description("주문 데이터 ID"),
                    fieldWithPath("data.message").description("주문 데이터 ID"),
                    fieldWithPath("data.optionId").description("주문 데이터 ID"),
                    fieldWithPath("data.quantity").description("주문 데이터 ID"),
                    fieldWithPath("data.productId").description("주문 데이터 ID")
                )
            ));
    }
    @Test
    void getOrderListPage() throws Exception, IllegalEmailException {
        // Given
        Member member = new Member("test@example.com", "password");
        member.setId(1L);

        Product product = new Product("Test Product", 10000, "image.jpg");
        product.setId(1L);

        int page = 0;
        int size = 10;
        String sortStr = "orderDateTime,DESC";


        String token = Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("email", member.getEmail())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();

        List<Order> orders = List.of(new Order(), new Order());
        Page<Order> orderPage = new PageImpl<>(orders, PageRequest.of(page, size, Sort.by(Direction.DESC, "orderDateTime")), orders.size());

        given(loginMemberArgumentResolver.supportsParameter(any(MethodParameter.class)))
            .willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
            .willReturn(member);

        given(orderService.getOrderListPage( any(Member.class),eq(page), eq(size), eq("orderDateTime"),
            eq(Direction.DESC)))
            .willReturn(orderPage);

        // When & Then
        mockMvc.perform(get("/api/orders")
                .header("Authorization", "Bearer " + token)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sortStr))
            .andExpect(status().isOk()).andDo(print())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("주문 리스트 조회 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.data.content", hasSize(2)))
            .andDo(document("get-order-list",
                requestHeaders(
                    headerWithName("Authorization").description("Bearer JWT token")
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("httpStatus").description("HTTP 상태"),
                    fieldWithPath("data.content").description("주문 목록"),
                    fieldWithPath("data.content[].id").description("주문 ID").optional(),
                    fieldWithPath("data.content[].localDateTime").description("주문 일시").optional(),
                    fieldWithPath("data.content[].message").description("주문 메시지").optional(),
                    fieldWithPath("data.content[].optionId").description("옵션 ID").optional(),
                    fieldWithPath("data.content[].quantity").description("주문 수량"),
                    fieldWithPath("data.content[].productId").description("상품 ID").optional(),
                    fieldWithPath("data.totalElements").description("전체 주문 수"),
                    fieldWithPath("data.totalPages").description("전체 페이지 수"),
                    fieldWithPath("data.size").description("페이지 크기"),
                    fieldWithPath("data.number").description("현재 페이지 번호"),
                    fieldWithPath("data.first").description("첫 페이지 여부"),
                    fieldWithPath("data.last").description("마지막 페이지 여부"),
                    fieldWithPath("data.numberOfElements").description("현재 페이지의 요소 수"),
                    fieldWithPath("data.empty").description("결과가 비어있는지 여부"),
                    fieldWithPath("data.sort.empty").description("정렬 정보가 비어있는지 여부"),
                    fieldWithPath("data.sort.sorted").description("정렬되어 있는지 여부"),
                    fieldWithPath("data.sort.unsorted").description("정렬되어 있지 않은지 여부"),
                    fieldWithPath("data.pageable.pageNumber").description("현재 페이지 번호"),
                    fieldWithPath("data.pageable.pageSize").description("페이지 크기"),
                    fieldWithPath("data.pageable.sort.empty").description("정렬 정보가 비어있는지 여부"),
                    fieldWithPath("data.pageable.sort.sorted").description("정렬되어 있는지 여부"),
                    fieldWithPath("data.pageable.sort.unsorted").description("정렬되어 있지 않은지 여부"),
                    fieldWithPath("data.pageable.offset").description("현재 페이지의 시작 오프셋"),
                    fieldWithPath("data.pageable.paged").description("페이징 사용 여부"),
                    fieldWithPath("data.pageable.unpaged").description("페이징 미사용 여부")

                )
            ));

    }
}