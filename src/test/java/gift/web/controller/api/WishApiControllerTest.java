package gift.web.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import gift.domain.WishProduct;
import gift.domain.vo.Email;
import gift.mock.MockLoginMemberArgumentResolver;
import gift.service.WishProductService;
import gift.web.dto.request.wishproduct.CreateWishProductRequest;
import gift.web.dto.request.wishproduct.UpdateWishProductRequest;
import gift.web.dto.response.wishproduct.CreateWishProductResponse;
import gift.web.dto.response.wishproduct.ReadWishProductResponseByPromise;
import gift.web.dto.response.wishproduct.UpdateWishProductResponse;
import io.swagger.v3.core.util.Json;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
class WishApiControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private RestDocumentationResultHandler restDocs;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WishProductService wishProductService;

    @Autowired
    private JwtProvider jwtProvider;

    private String accessToken;

    private static final String BASE_URL = "/api/wishes";

    @BeforeEach
    void setUp(
        final RestDocumentationContextProvider provider
    ) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new WishApiController(wishProductService))
            .setCustomArgumentResolvers(new MockLoginMemberArgumentResolver(), new PageableHandlerMethodArgumentResolver())
            .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
            .alwaysDo(restDocs)
            .build();

        Member member = new Builder().id(1L).name("회원01").email(Email.from("member01@gmail.com"))
            .build();
        accessToken = jwtProvider.generateToken(member).getValue();
    }

    @Test
    @DisplayName("위시 상품 조회")
    void readWishProduct() throws Exception {
        Page<ReadWishProductResponseByPromise> response =
            new PageImpl(
                List.of(ReadWishProductResponseByPromise.fromEntity(new WishProduct.Builder()
                    .id(1L)
                    .product(new Product.Builder()
                        .id(1L)
                        .name("product01")
                        .price(1000)
                        .imageUrl(StringToUrlConverter.convert("https://via.placeholder.com/150"))
                        .productOptions(List.of(
                            new ProductOption.Builder().id(1L).name("option01").stock(1000)
                                .build()))
                        .category(new Category.Builder().id(1L).name("category01").build())
                        .build())
                    .quantity(5)
                    .build())), PageRequest.of(0, 10), 1);


        given(wishProductService.readAllWishProducts(any(Long.class), any()))
            .willReturn(response);

        mockMvc
            .perform(
                get(BASE_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    queryParameters(
                        parameterWithName("page").optional().description("페이지 번호"),
                        parameterWithName("size").optional().description("페이지 크기")
                    ),
                    responseFields(
                        fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("페이지 정보"),
                        fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                        fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                        fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("페이지 오프셋"),
                        fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 여부"),
                        fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징 안함"),

                        fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                        fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보 없음"),
                        fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                        fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 안함"),

                        fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("위시 상품 식별자"),
                        fieldWithPath("content[].product").type(JsonFieldType.OBJECT).description("상품 정보"),
                        fieldWithPath("content[].product.id").type(JsonFieldType.NUMBER).description("상품 식별자"),
                        fieldWithPath("content[].product.name").type(JsonFieldType.STRING).description("상품 이름"),
                        fieldWithPath("content[].product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                        fieldWithPath("content[].product.imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                        fieldWithPath("content[].product.categoryId").type(JsonFieldType.NUMBER).description("상품의 카테고리 식별자"),

                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                        fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 요소 수"),
                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                        fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                        fieldWithPath("number").type(JsonFieldType.NUMBER).description("페이지 번호"),

                        fieldWithPath("sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                        fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보 없음"),
                        fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                        fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 안함"),

                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 요소 수"),
                        fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("비어있는지 여부")
                    )
                )
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
                post(BASE_URL)
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
    @DisplayName("위시 상품 수정")
    void updateWishProduct() throws Exception {
        UpdateWishProductRequest request = new UpdateWishProductRequest(5);
        String content = Json.mapper().writeValueAsString(request);

        given(wishProductService.updateWishProduct(any(Long.class), any()))
            .willReturn(new UpdateWishProductResponse(1L, 1L, "product01", 1000, 5, "https://via.placeholder.com/150"));

        mockMvc
            .perform(
                put(BASE_URL + "/{wishProductId}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수량")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("위시 상품 식별자"),
                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 식별자"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("재고 수량"),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 URL")
                    )
                )
            );
    }

    @Test
    @DisplayName("위시 상품 삭제")
    void deleteWishProduct() throws Exception {
        mockMvc
            .perform(
                delete(BASE_URL + "/{wishProductId}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isNoContent())
            .andDo(
                restDocs.document()
            );
    }
}