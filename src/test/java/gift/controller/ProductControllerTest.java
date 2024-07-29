package gift.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
import gift.dto.ProductDto;
import gift.exception.NonIntegerPriceException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.security.LoginMemberArgumentResolver;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import java.util.ArrayList;
import java.util.List;
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
    void getAllProductsTest() throws Exception {
        // given
        given(productService.getAllProducts()).willReturn(productList);

        // when & then
        mockMvc.perform(get("/products/all").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("상품 전체 조회 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("getAllProductsTest", responseFields(
                fieldWithPath("result").description("API 호출 결과"),
                fieldWithPath("message").description("The Welcome message for the user"),
                fieldWithPath("httpStatus").description("HTTP 응답 상태")
            )));
    }

    @Test
    void addProductTest() throws Exception, NonIntegerPriceException {
        // given
        var productDto = new ProductDto(product.getName(), product.getPrice(),
            product.getImageUrl(), category.getId());
        given(productService.createProduct(productDto)).willReturn(product);

        // when & then
        mockMvc.perform(post("/product/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
            .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/"))
            .andDo(document("add-product",
                requestFields(
                    fieldWithPath("name").description("상품 이름"),
                    fieldWithPath("price").description("상품 가격"),
                    fieldWithPath("imageUrl").description("상품 이미지 URL"),
                    fieldWithPath("categoryId").description("카테고리 ID"),
                    fieldWithPath("optionId").description("옵션 Id")
                )
            ));

    }

    @Test
    void updateProductTest() throws Exception {
        // given
        var newProduct = new Product(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getWishList(), product.getCategory(), product.getOptionList());
        newProduct.setPrice(2000);
        given(productService.updateProduct(newProduct)).willReturn(newProduct);

        // when & then
        mockMvc.perform(post("/product/update").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
            .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"))
            .andDo(document("update-product",
                requestFields(
                    fieldWithPath("id").description("상품 ID"),
                    fieldWithPath("name").description("상품 이름"),
                    fieldWithPath("price").description("상품 가격"),
                    fieldWithPath("imageUrl").description("상품 이미지 URL"),
                    fieldWithPath("wishList").description("위시리스트").optional(),
                    fieldWithPath("category.id").description("카테고리 ID"),
                    fieldWithPath("category.name").description("카테고리 이름")
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
                get("/product/delete/{id}", product.getId()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"))
            .andDo(document("delete-product",
                pathParameters(
                    parameterWithName("id").description("삭제할 상품의 ID")
                ),
                responseHeaders(
                    headerWithName("Location").description("리다이렉션 URL")
                )
            ));
    }

    @Test
    void deleteProductFormFailureTest() throws Exception {
        // given
        given(productService.deleteProduct(product.getId())).willReturn(false);

        // when & then
        mockMvc.perform(get("/product/delete/{id}", product.getId()))
            .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"))
            .andExpect(flash().attributeCount(0));
    }
}