package gift.controller;

import static capital.scalable.restdocs.AutoDocumentation.requestParameters;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.IllegalEmailException;
import gift.category.model.Category;
import gift.member.dto.MemberRequest;
import gift.member.model.Member;
import gift.member.service.MemberService;
import gift.product.model.Product;
import gift.wish.dto.WishRequest;
import gift.wish.model.Wish;
import gift.security.LoginMemberArgumentResolver;
import gift.product.service.ProductService;
import gift.wish.repository.WishRepository;
import gift.wish.service.WishService;
import gift.wish.controller.WishController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
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
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(WishController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class WishControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishService wishService;

    @MockBean
    private ProductService productService;

    @Autowired
    private WebApplicationContext context;

    @Value("${secret.key}")
    String secretKey;

    @MockBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @Autowired
    private ObjectMapper objectMapper;

    private Wish wish;
    private Product product;
    private Member member;
    private Category category;


    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) throws IllegalEmailException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .alwaysDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .build();

        member = new Member("Email@test.com", "password");
        member.setId(1L);

        product = new Product("product", 10000, "image.jpg");
        category = new Category(1L, "category", Collections.singletonList(product));
        product.setCategory(category);
        wish = new Wish(product, member);
        product.setWishList(Collections.singletonList(wish));
        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(
            member);
    }

    @Test
    void getWishes() throws Exception, IllegalEmailException {
        // Given
        Member member = new Member("test@example.com", "password");
        member.setId(1L);

        Product product = new Product("Test Product", 10000, "image.jpg");
        product.setId(1L);

        Wish wish = new Wish(product, member);
        wish.setId(1L);

        String token = Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("email", member.getEmail())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();

        List<Wish> wishes = Collections.singletonList(wish);
        Page<Wish> wishPage = new PageImpl<>(wishes,
            PageRequest.of(0, 10, Sort.by(Direction.DESC, "orderDateTime")), wishes.size());

        given(loginMemberArgumentResolver.supportsParameter(any(MethodParameter.class)))
            .willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
            .willReturn(member);

        given(wishService.getWishListPage(eq(member), eq(0), eq(10), eq("orderDateTime"),
            eq(Direction.DESC)))
            .willReturn(wishPage);

        // When & Then
        mockMvc.perform(get("/api/wishes")
                .header("Authorization", "Bearer " + token)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "orderDateTime,DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("장바구니 조회 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.data.content", hasSize(1)))
            .andExpect(jsonPath("$.data.content[0].id").value(1))
            .andExpect(jsonPath("$.data.content[0].productName").value("Test Product"))
            .andExpect(jsonPath("$.data.content[0].memberId").value(1))
            .andExpect(jsonPath("$.data.totalElements").value(1))
            .andExpect(jsonPath("$.data.totalPages").value(1))
            .andDo(document("get-wishes",
                requestHeaders(
                    headerWithName("Authorization").description("Bearer JWT token")
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("호출 내용"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data.content").description("위시 리스트"),
                    fieldWithPath("data.content[].id").description("위시 ID"),
                    fieldWithPath("data.content[].productName").description("상품 이름"),
                    fieldWithPath("data.content[].memberId").description("회원 ID"),
                    fieldWithPath("data.totalElements").description("전체 위시 수"),
                    fieldWithPath("data.totalPages").description("전체 페이지 수"),
                    fieldWithPath("data.number").description("현재 페이지 번호"),
                    fieldWithPath("data.size").description("페이지 크기"),
                    fieldWithPath("data.numberOfElements").description("현재 페이지의 위시 수"),
                    fieldWithPath("data.first").description("첫 페이지 여부"),
                    fieldWithPath("data.last").description("마지막 페이지 여부"),
                    fieldWithPath("data.sort.empty").description("정렬 정보 존재 여부"),
                    fieldWithPath("data.sort.sorted").description("정렬된 상태 여부"),
                    fieldWithPath("data.sort.unsorted").description("정렬되지 않은 상태 여부"),
                    fieldWithPath("data.pageable.pageNumber").description("현재 페이지 번호"),
                    fieldWithPath("data.pageable.pageSize").description("페이지 크기"),
                    fieldWithPath("data.pageable.sort.empty").description("정렬 정보 존재 여부"),
                    fieldWithPath("data.pageable.sort.sorted").description("정렬된 상태 여부"),
                    fieldWithPath("data.pageable.sort.unsorted").description("정렬되지 않은 상태 여부"),
                    fieldWithPath("data.pageable.offset").description("현재 페이지의 시작 오프셋"),
                    fieldWithPath("data.pageable.paged").description("페이징 사용 여부"),
                    fieldWithPath("data.pageable.unpaged").description("페이징 미사용 여부"),
                    fieldWithPath("data.empty").description("결과가 비어있는지 여부")
                )
            ));


    }

    @Test
    void addWishTest() throws Exception, IllegalEmailException {
        // Given
        Long productId = 1L;

        Member member1 = new Member("test@example.com", "password");

        member1.setId(1L);

        Product product = new Product("Test Product", 10000, "image.jpg");
        product.setId(productId);

        WishRequest wishRequest = new WishRequest();
        wishRequest.setProductId(productId);

        Wish wish = new Wish(product, member1);
        wish.setId(1L);

        String token = Jwts.builder()
            .setSubject(member1.getId().toString())
            .claim("email", member1.getEmail())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();

        // Mock LoginMemberArgumentResolver
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
            .willReturn(member1);

        given(wishService.addWish(eq(productId), eq(member1))).willReturn(wish);

        // When & Then
        mockMvc.perform(post("/api/wishes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wishRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("장바구니 추가 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(print())
            .andDo(document("add-wish",
                requestFields(
                    fieldWithPath("productId").description("추가할 상품의 ID")
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("호출 내용"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data").description("응답 내용"),
                    fieldWithPath("data.id").description("위시리스트 ID"),
                    fieldWithPath("data.productName").description("위시리스트 상품명"),
                    fieldWithPath("data.memberId").description("회원 ID")
                )));

    }

    @Test
    void removeWish() throws Exception, IllegalEmailException {
        // Given
        Long wishId = 1L;
        Member member = new Member("test@example.com", "password");
        member.setId(1L);

        String token = Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("email", member.getEmail())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();

        given(loginMemberArgumentResolver.supportsParameter(any(MethodParameter.class)))
            .willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
            .willReturn(member);

        given(wishService.removeWish(eq(wishId), eq(member)))
            .willReturn(true);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/wishes/{id}", wishId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("장바구니 제거 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("remove-wish",
                pathParameters(
                    parameterWithName("id").description("삭제할 위시 ID")
                ),
                requestHeaders(
                    headerWithName("Authorization").description("Bearer JWT token")
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("호출 내용"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data").description("응답 데이터 (항상 null)")
                )
            ));

        verify(wishService).removeWish(eq(wishId), eq(member));
    }
}