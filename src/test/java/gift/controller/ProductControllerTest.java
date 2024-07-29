package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.service.ProductService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
public class ProductControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private CategoryRepository categoryRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private Category category;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        category = new Category("교환권", "#aaa", "https://asdf.com", "description");
        categoryRepository.save(category);
    }

    @Test
    @DisplayName("product post api 테스트")
    void create() throws Exception {
        Product product = new Product("상품 이름", 1000, "https://asdf", category);
        ProductRequest request = new ProductRequest(1L, "상품 이름", 1000, "https://asdf", 1L);
        String requestJson = objectMapper.writeValueAsString(request);
        when(productService.makeProduct(any())).thenReturn(product);

        this.mockMvc.perform(post("/api/products")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("product-create",
                        requestFields(
                                fieldWithPath("id").description("상품 아이디"),
                                fieldWithPath("name").description("상품 이름"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("imageUrl").description("상품 url"),
                                fieldWithPath("categoryId").description("카테고리 id")
                        )));

    }

    @Test
    @DisplayName("product getProduct api 테스트")
    void findAll() throws Exception {
        List<ProductResponse> productResponses = Lists.newArrayList(
                new ProductResponse(1L, "상품 이름", 1000, "https://asdf", category),
                new ProductResponse(2L, "상품 이름", 1000, "https://asdf", category)
        );
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductResponse> productResponsePage = new PageImpl<>(productResponses, pageRequest, productResponses.size());

        when(productService.getAllProducts(pageRequest)).thenReturn(productResponsePage);

        this.mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("product-get-all",
                        responseFields(
                                fieldWithPath("content[].id").description("상품 아이디"),
                                fieldWithPath("content[].name").description("상품 이름"),
                                fieldWithPath("content[].price").description("상품 가격"),
                                fieldWithPath("content[].imageUrl").description("상품 url"),
                                fieldWithPath("content[].category.id").description("카테고리 아이디").optional(),
                                fieldWithPath("content[].category.name").description("카테고리 이름"),
                                fieldWithPath("content[].category.color").description("카테고리 색상"),
                                fieldWithPath("content[].category.imageUrl").description("카테고리 이미지 URL"),
                                fieldWithPath("content[].category.description").description("카테고리 설명"),
                                fieldWithPath("page.size").description("페이지 크기"),
                                fieldWithPath("page.number").description("현재 페이지 번호"),
                                fieldWithPath("page.totalElements").description("총 요소 개수"),
                                fieldWithPath("page.totalPages").description("총 페이지 개수")
                        )));

    }

    @Test
    @DisplayName("product getProductById api 테스트")
    void findById() throws Exception {
        ProductResponse response = new ProductResponse(1L, "상품 이름", 1000, "https://asdfadf", category);
        when(productService.getProduct(anyLong())).thenReturn(response);

        this.mockMvc.perform(get("/api/products/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("product-get-one",
                        pathParameters(
                                parameterWithName("id").description("상품 Id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("상품 아이디"),
                                fieldWithPath("name").description("상품 이름"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("imageUrl").description("상품 url"),
                                fieldWithPath("category.id").description("카테고리 id"),
                                fieldWithPath("category.name").description("카테고리 이름"),
                                fieldWithPath("category.color").description("카테고리 색상"),
                                fieldWithPath("category.imageUrl").description("카테고리 imageUrl"),
                                fieldWithPath("category.description").description("카테고리 상세 설명")
                        )
                ));
    }

    @Test
    @DisplayName("product update api 테스트")
    void update() throws Exception {
        ProductRequest request = new ProductRequest(1L, "상품 이름", 1000, "https://asdf", 1L);
        String requestJson = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(put("/api/products")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("product-update",
                        requestFields(
                                fieldWithPath("id").description("상품 아이디"),
                                fieldWithPath("name").description("상품 이름"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("imageUrl").description("상품 url"),
                                fieldWithPath("categoryId").description("카테고리 id")
                        )
                ));
    }

    @Test
    @DisplayName("product delete api 테스트")
    void remove() throws Exception {
        this.mockMvc.perform(delete("/api/products/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(document("product-delete",
                        pathParameters(
                                parameterWithName("id").description("상품 Id")
                        )
                ));
    }

}
