package gift.controller;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.OptionNotFoundException;
import gift.product.dto.ProductRequest;
import gift.exception.NonIntegerPriceException;
import gift.category.model.Category;
import gift.option.model.Option;
import gift.product.model.Product;
import gift.product.controller.ProductController;
import gift.security.LoginMemberArgumentResolver;
import gift.category.service.CategoryService;
import gift.option.service.OptionService;
import gift.product.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ProductController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private OptionService optionService;


    @MockBean
    private CategoryService categoryService;

    @MockBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private WebApplicationContext context;


    private Product product;
    private Category category;
    private List<Product> productList = new ArrayList<>();

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .alwaysDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .build();

        product = new Product("상품", 10000, "image.jpg");
        category = new Category(1L, "카테고리", List.of(product));
        product.setId(1L);
        product.setCategory(category);
        product.setOptionList(List.of(new Option("option", 1)));
        productList.add(product);
    }

    @Test
    void getPaginationTest() throws Exception {
        // Given
        int page = 0;
        int size = 10;
        String sort = "orderDateTime,DESC";
        Long categoryId = 1L;

        List<Product> products = List.of(
            new Product("Product 1", 10000, "image1.jpg"),
            new Product("Product 2", 20000, "image2.jpg")
        );
        Page<Product> productPage = new PageImpl<>(products,
            PageRequest.of(page, size, Sort.by(Direction.DESC, "orderDateTime")), products.size());

        given(productService.getProductList(eq(page), eq(size), eq("orderDateTime"),
            eq(Direction.DESC)))
            .willReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort)
                .param("categoryId", String.valueOf(categoryId))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andDo(print())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("상품 조회 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.data.content", hasSize(2)))
            .andExpect(jsonPath("$.data.content[0].name").value("Product 1"))
            .andExpect(jsonPath("$.data.content[1].name").value("Product 2"))
            .andExpect(jsonPath("$.data.totalPages").value(1))
            .andExpect(jsonPath("$.data.totalElements").value(2))
            .andExpect(jsonPath("$.data.size").value(10))
            .andExpect(jsonPath("$.data.number").value(0))
            .andDo(print())
            .andDo(document("get-products-pagination",
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data.content").description("상품 목록"),
                    fieldWithPath("data.content[].id").description("상품 ID"),
                    fieldWithPath("data.content[].name").description("상품 이름"),
                    fieldWithPath("data.content[].price").description("상품 가격"),
                    fieldWithPath("data.content[].imageUrl").description("상품 이미지 URL"),
                    fieldWithPath("data.totalPages").description("전체 페이지 수"),
                    fieldWithPath("data.totalElements").description("전체 상품 수"),
                    fieldWithPath("data.size").description("페이지 크기"),
                    fieldWithPath("data.number").description("현재 페이지 번호"),
                    fieldWithPath("data.first").description("첫 페이지 여부"),
                    fieldWithPath("data.last").description("마지막 페이지 여부"),
                    fieldWithPath("data.numberOfElements").description("현재 페이지의 요소 수"),
                    fieldWithPath("data.empty").description("페이지가 비어있는지 여부"),
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

    @Test
    void addProductTest() throws Exception, NonIntegerPriceException, OptionNotFoundException {
        // given
        var productDto = new ProductRequest(product.getName(), product.getPrice(),
            product.getImageUrl(), category.getId());
        System.out.println(productDto);
        given(productService.createProduct(productDto)).willReturn(product);
        System.out.println(product);
        // when & then
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
            .andExpect(status().isOk()).andDo(print())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("상품 추가 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("add-product",
                requestFields(
                    fieldWithPath("name").description("상품 이름"),
                    fieldWithPath("price").description("상품 가격"),
                    fieldWithPath("imageUrl").description("상품 이미지 URL"),
                    fieldWithPath("categoryId").description("카테고리 ID"),
                    fieldWithPath("optionId").description("옵션 Id")
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data").description("상품")
                )
            ));


    }

    @Test
    void updateProductTest() throws Exception {
        // given
        var newProduct = new Product(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getWishList(), product.getCategory(),
            product.getOptionList());
        newProduct.setPrice(2000);

        var newProductRequest = new ProductRequest(newProduct.getName(), newProduct.getPrice(),
            newProduct.getImageUrl(), newProduct.getCategory().getId());

        given(productService.updateProduct(any(Long.class), any(ProductRequest.class))).willReturn(
            newProduct);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProductRequest)))
            .andExpect(status().isOk()).andDo(print())
            .andDo(document("update-product",
                requestFields(
                    fieldWithPath("name").description("상품 이름"),
                    fieldWithPath("price").description("상품 가격"),
                    fieldWithPath("imageUrl").description("상품 이미지 URL"),
                    fieldWithPath("categoryId").description("카테고리 ID"),
                    fieldWithPath("optionId").description("옵션 ID")
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data").description("상품"),
                    fieldWithPath("data.id").description("상품 ID"),
                    fieldWithPath("data.name").description("상품 ID"),
                    fieldWithPath("data.price").description("상품 ID"),
                    fieldWithPath("data.imageUrl").description("상품 ID")
                )
            ));
    }

    @Test
    void deleteProductTest() throws Exception {
        // given
        given(productService.deleteProduct(any(Long.class))).willReturn(true);

        // when & then
        mockMvc.perform(
                RestDocumentationRequestBuilders.
                    delete("/api/products/{id}", product.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andDo(print())
            .andDo(document("delete-product",
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data").description("응답 null")
                )
            ));
    }

}