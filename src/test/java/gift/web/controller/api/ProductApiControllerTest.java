package gift.web.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.authentication.token.JwtProvider;
import gift.config.RestDocsConfiguration;
import gift.converter.StringToUrlConverter;
import gift.domain.Category;
import gift.domain.Member;
import gift.domain.Member.Builder;
import gift.domain.Product;
import gift.domain.ProductOption;
import gift.domain.vo.Color;
import gift.domain.vo.Email;
import gift.mock.MockLoginMemberArgumentResolver;
import gift.service.OrderService;
import gift.service.ProductOptionService;
import gift.service.ProductService;
import gift.service.WishProductService;
import gift.web.dto.request.order.CreateOrderRequest;
import gift.web.dto.request.product.CreateProductRequest;
import gift.web.dto.request.product.UpdateProductRequest;
import gift.web.dto.request.productoption.CreateProductOptionRequest;
import gift.web.dto.request.productoption.UpdateProductOptionRequest;
import gift.web.dto.request.wishproduct.CreateWishProductRequest;
import gift.web.dto.response.category.ReadCategoryResponse;
import gift.web.dto.response.order.OrderResponse;
import gift.web.dto.response.product.CreateProductResponse;
import gift.web.dto.response.product.ReadAllProductsResponse;
import gift.web.dto.response.product.ReadProductResponse;
import gift.web.dto.response.product.UpdateProductResponse;
import gift.web.dto.response.productoption.CreateProductOptionResponse;
import gift.web.dto.response.productoption.ReadAllProductOptionsResponse;
import gift.web.dto.response.productoption.ReadProductOptionResponse;
import gift.web.dto.response.productoption.UpdateProductOptionResponse;
import gift.web.dto.response.wishproduct.CreateWishProductResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
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
class ProductApiControllerTest {

    private MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private ProductService productService;
    @MockBean
    private WishProductService wishProductService;
    @MockBean
    private ProductOptionService productOptionService;
    @MockBean
    private OrderService orderService;

    private String accessToken;

    private static final String BASE_URL = "/api/products";

    @BeforeEach
    void setUp(
        final RestDocumentationContextProvider provider
    ) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new ProductApiController(productService, wishProductService, productOptionService, orderService))
            .setCustomArgumentResolvers(new MockLoginMemberArgumentResolver(), new PageableHandlerMethodArgumentResolver())
            .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(restDocs)
            .build();

        Member member = new Builder().id(1L).name("회원01").email(Email.from("member01@gmail.com"))
            .build();
        accessToken = jwtProvider.generateToken(member).getValue();
    }

    @Test
    @DisplayName("전체 상품 조회")
    void readAllProducts() throws Exception {
        given(productService.readAllProducts(any(Pageable.class)))
            .willReturn(ReadAllProductsResponse.from(
                List.of(new ReadProductResponse(1L, "상품01", 1000, "https://via.placeholder.com/150",
                    List.of(new ReadProductOptionResponse(1L, "상품 옵션 01", 100)),
                    new ReadCategoryResponse(1L, "카테고리01", "카테고리01 입니다", "https://via.placeholder.com/150", "#FFFFFF")))
            ));

        mockMvc
            .perform(
                get(BASE_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .param("page", "0")
                    .param("size", "10")
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    queryParameters(
                        parameterWithName("page").optional().description("페이지 번호"),
                        parameterWithName("size").optional().description("페이지 크기")
                    ),
                    responseFields(
                        fieldWithPath("products[].id").type(JsonFieldType.NUMBER).description("상품 ID"),
                        fieldWithPath("products[].name").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("products[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                        fieldWithPath("products[].imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL"),
                        fieldWithPath("products[].options").type(JsonFieldType.ARRAY).description("상품 옵션 목록"),
                        fieldWithPath("products[].options[].id").type(JsonFieldType.NUMBER).description("상품 옵션 ID"),
                        fieldWithPath("products[].options[].name").type(JsonFieldType.STRING).description("상품 옵션명"),
                        fieldWithPath("products[].options[].stock").type(JsonFieldType.NUMBER).description("상품 옵션 재고"),
                        fieldWithPath("products[].category.id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("products[].category.name").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("products[].category.description").type(JsonFieldType.STRING).description("카테고리 설명"),
                        fieldWithPath("products[].category.imageUrl").type(JsonFieldType.STRING).description("카테고리 이미지 URL"),
                        fieldWithPath("products[].category.color").type(JsonFieldType.STRING).description("카테고리 색상")
                    )
                )
            );
    }

//    todo requestParam 만 다른 경로에 대해 테스트 코드 동작시키기
//    @Test
//    @DisplayName("카테고리로 상품 조회")
//    void readProductsByCategoryId() throws Exception {
//        given(productService.readProductsByCategoryId(any(Long.class), any(Pageable.class)))
//            .willReturn(ReadAllProductsResponse.from(
//                List.of(new ReadProductResponse(1L, "상품01", 1000, "https://via.placeholder.com/150",
//                    List.of(new ReadProductOptionResponse(1L, "상품 옵션 01", 100)),
//                    new ReadCategoryResponse(1L, "카테고리01", "카테고리01 입니다", "https://via.placeholder.com/150", "#FFFFFF")))
//            ));
//
//        mockMvc
//            .perform(
//                get(BASE_URL)
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//            )
//            .andExpect(status().isOk())
//            .andDo(
//                restDocs.document(
//                    queryParameters(
//                        parameterWithName("page").optional().description("페이지 번호"),
//                        parameterWithName("size").optional().description("페이지 크기"),
//                        parameterWithName("categoryId").description("카테고리 아이디")
//                    ),
//                    responseFields(
//                        fieldWithPath("products[].id").type(JsonFieldType.NUMBER).description("상품 ID"),
//                        fieldWithPath("products[].name").type(JsonFieldType.STRING).description("상품명"),
//                        fieldWithPath("products[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
//                        fieldWithPath("products[].imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL"),
//                        fieldWithPath("products[].productOptions").type(JsonFieldType.ARRAY).description("상품 옵션 목록"),
//                        fieldWithPath("products[].productOptions[].id").type(JsonFieldType.NUMBER).description("상품 옵션 ID"),
//                        fieldWithPath("products[].productOptions[].name").type(JsonFieldType.STRING).description("상품 옵션명"),
//                        fieldWithPath("products[].productOptions[].stock").type(JsonFieldType.NUMBER).description("상품 옵션 재고"),
//                        fieldWithPath("products[].category.id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
//                        fieldWithPath("products[].category.name").type(JsonFieldType.STRING).description("카테고리명"),
//                        fieldWithPath("products[].category.description").type(JsonFieldType.STRING).description("카테고리 설명"),
//                        fieldWithPath("products[].category.imageUrl").type(JsonFieldType.STRING).description("카테고리 이미지 URL"),
//                        fieldWithPath("products[].category.color").type(JsonFieldType.STRING).description("카테고리 색상")
//                    )
//                )
//            );
//    }

    @Test
    @DisplayName("상품 생성")
    void createProduct() throws Exception {
        CreateProductRequest request = new CreateProductRequest("상품01", 1000,
            "https://via.placeholder.com/150", 1L,
            List.of(new CreateProductOptionRequest("상품 옵션 01", 100)));
        String content = objectMapper.writeValueAsString(request);

        given(productService.createProduct(any(CreateProductRequest.class)))
            .willReturn(new CreateProductResponse(1L, "상품01", 1000, "https://via.placeholder.com/150", List.of(new ProductOption.Builder().id(1L).productId(1L).name("상품 옵션 01").stock(100).build())) );

        mockMvc
            .perform(
                post(BASE_URL)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isCreated())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL"),
                        fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("productOptions").type(JsonFieldType.ARRAY).description("상품 옵션 목록"),
                        fieldWithPath("productOptions[].name").type(JsonFieldType.STRING).description("상품 옵션명"),
                        fieldWithPath("productOptions[].stock").type(JsonFieldType.NUMBER).description("상품 옵션 재고")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL"),
                        fieldWithPath("options").type(JsonFieldType.ARRAY).description("상품 옵션 목록"),
                        fieldWithPath("options[].id").type(JsonFieldType.NUMBER).description("상품 옵션 ID"),
                        fieldWithPath("options[].name").type(JsonFieldType.STRING).description("상품 옵션명"),
                        fieldWithPath("options[].stock").type(JsonFieldType.NUMBER).description("상품 옵션 재고"),
                        fieldWithPath("options[].productId").type(JsonFieldType.NUMBER).description("상품 옵션 상품 ID")
                    )
                )
            );
    }

    @Test
    @DisplayName("단일 상품 조회")
    void readProduct() throws Exception {
        given(productService.readProductById(any(Long.class)))
            .willReturn(new ReadProductResponse(1L, "상품01", 1000, "https://via.placeholder.com/150",
                List.of(new ReadProductOptionResponse(1L, "상품 옵션 01", 100)),
                new ReadCategoryResponse(1L, "카테고리01", "카테고리01 입니다", "https://via.placeholder.com/150", "#FFFFFF")));

        mockMvc
            .perform(
                get(BASE_URL + "/{productId}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL"),
                        fieldWithPath("options").type(JsonFieldType.ARRAY).description("상품 옵션 목록"),
                        fieldWithPath("options[].id").type(JsonFieldType.NUMBER).description("상품 옵션 ID"),
                        fieldWithPath("options[].name").type(JsonFieldType.STRING).description("상품 옵션명"),
                        fieldWithPath("options[].stock").type(JsonFieldType.NUMBER).description("상품 옵션 재고"),
                        fieldWithPath("category.id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("category.name").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("category.description").type(JsonFieldType.STRING).description("카테고리 설명"),
                        fieldWithPath("category.imageUrl").type(JsonFieldType.STRING).description("카테고리 이미지 URL"),
                        fieldWithPath("category.color").type(JsonFieldType.STRING).description("카테고리 색상")
                    )
                )
            );
    }

    @Test
    @DisplayName("상품 수정")
    void updateProduct() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest("상품01", 1000, "https://via.placeholder.com/150");

        String content = objectMapper.writeValueAsString(request);

        given(productService.updateProduct(any(Long.class), any()))
            .willReturn(UpdateProductResponse.from(new Product.Builder()
                .id(1L)
                .name("상품01")
                .price(1000)
                .productOptions(
                    List.of(
                        new ProductOption.Builder()
                            .id(1L)
                            .productId(1L)
                            .name("상품 옵션 01")
                            .stock(100)
                            .build()
                    ))
                .imageUrl(StringToUrlConverter.convert("https://via.placeholder.com/150"))
                .category(new Category.Builder()
                    .id(1L)
                    .name("카테고리01")
                    .color(Color.from("#FFFFFF"))
                    .description("카테고리01 입니다")
                    .imageUrl(StringToUrlConverter.convert("https://via.placeholder.com/150"))
                    .build())
                .build()));

        mockMvc
            .perform(
                put(BASE_URL + "/{productId}", 1L)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL"),
                        fieldWithPath("category.id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("category.name").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("category.description").type(JsonFieldType.STRING).description("카테고리 설명"),
                        fieldWithPath("category.imageUrl").type(JsonFieldType.STRING).description("카테고리 이미지 URL"),
                        fieldWithPath("category.color").type(JsonFieldType.STRING).description("카테고리 색상")
                    )
                )
            );
    }

    @Test
    @DisplayName("상품 삭제")
    void deleteProduct() throws Exception {
        mockMvc
            .perform(
                delete(BASE_URL + "/{productId}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isNoContent())
            .andDo(
                restDocs.document()
            );
    }

    @Test
    @DisplayName("위시 상품 추가")
    void createWishProduct() throws Exception {
        CreateWishProductRequest request = new CreateWishProductRequest(1L, 1);
        String content = objectMapper.writeValueAsString(request);

        given(wishProductService.createWishProduct(any(Long.class), any()))
            .willReturn(new CreateWishProductResponse(1L, 1));

        mockMvc
            .perform(
                post(BASE_URL + "/wish")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수량")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("위시 상품 ID"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수량")
                    )
                )
            );
    }

    @Test
    @DisplayName("상품 옵션 조회")
    void readOptions() throws Exception {
        given(productOptionService.readAllOptions(any(Long.class)))
            .willReturn(ReadAllProductOptionsResponse.from(
                List.of(new ReadProductOptionResponse(1L, "상품 옵션 01", 100))
            ));

        mockMvc
            .perform(
                get(BASE_URL + "/{productId}/options", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    responseFields(
                        fieldWithPath("options[].id").type(JsonFieldType.NUMBER).description("상품 옵션 ID"),
                        fieldWithPath("options[].name").type(JsonFieldType.STRING).description("상품 옵션명"),
                        fieldWithPath("options[].stock").type(JsonFieldType.NUMBER).description("상품 옵션 재고")
                    )
                )
            );
    }

    @Test
    @DisplayName("상품 옵션 생성")
    void createOption() throws Exception {
        CreateProductOptionRequest request = new CreateProductOptionRequest(
            "상품 옵션 01", 100);
        String content = objectMapper.writeValueAsString(request);

        given(productOptionService.createOption(any(Long.class), any()))
            .willReturn(new CreateProductOptionResponse(1L, "상품 옵션 01", 100));

        mockMvc
            .perform(
                post(BASE_URL + "/{productId}/options", 1L)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isCreated())
            .andDo(
                restDocs.document(
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 옵션 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품 옵션명"),
                        fieldWithPath("stock").type(JsonFieldType.NUMBER).description("상품 옵션 재고")
                    )
                )
            );
    }

    @Test
    @DisplayName("상품 주문")
    void orderProduct() throws Exception {

        CreateOrderRequest request = new CreateOrderRequest(1L, 1, "message");
        String content = objectMapper.writeValueAsString(request);

        given(orderService.createOrder(any(String.class), any(Long.class), any(Long.class), any()))
            .willReturn(new OrderResponse(1L, 1L, 10, "message"));

        mockMvc
            .perform(
                post(BASE_URL + "/{productId}/order", 1L)
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
                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                        fieldWithPath("optionId").type(JsonFieldType.NUMBER).description("상품 옵션 ID"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("주문 수량"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지")
                    )
                )
            );
    }

    @Test
    @DisplayName("상품 옵션 수정")
    void updateOption() throws Exception {
        UpdateProductOptionRequest request = new UpdateProductOptionRequest(
            "상품 옵션 01", 100);
        String content = objectMapper.writeValueAsString(request);

        given(productOptionService.updateOption(any(Long.class), any(Long.class), any()))
            .willReturn(new UpdateProductOptionResponse(1L, "상품 옵션 01", 100));

        mockMvc
            .perform(
                put(BASE_URL + "/{productId}/options/{optionId}", 1L, 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품 옵션명"),
                        fieldWithPath("stock").type(JsonFieldType.NUMBER).description("상품 옵션 재고")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 옵션 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품 옵션명"),
                        fieldWithPath("stock").type(JsonFieldType.NUMBER).description("상품 옵션 재고")
                    )
                )
            );
    }

    @Test
    @DisplayName("상품 옵션 삭제")
    void deleteOption() throws Exception {
        mockMvc
            .perform(
                delete(BASE_URL + "/{productId}/options/{optionId}", 1L, 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isNoContent())
            .andDo(
                restDocs.document()
            );
    }
}