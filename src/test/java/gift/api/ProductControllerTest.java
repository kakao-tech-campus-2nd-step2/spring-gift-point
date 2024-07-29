package gift.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.global.security.JwtFilter;
import gift.global.security.JwtUtil;
import gift.product.application.OptionService;
import gift.product.dto.OptionRequest;
import gift.product.dto.OptionResponse;
import gift.product.entity.Category;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.product.api.ProductController;
import gift.product.application.ProductService;
import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;
import gift.product.util.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import testFixtures.CategoryFixture;
import testFixtures.ProductFixture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private JwtFilter jwtFilter;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private ProductService productService;
    @MockBean
    private OptionService optionService;
    private final String bearerToken = "Bearer token";

    private Category category;

    private OptionRequest optionRequest;

    @BeforeEach
    void setUp() {
        category = CategoryFixture.createCategory("상품권");
        optionRequest = new OptionRequest("옵션", 10);
    }

    @Test
    @DisplayName("상품 전체 조회 기능 테스트")
    void getAllProducts() throws Exception {
        List<ProductResponse> products = new ArrayList<>();
        ProductResponse productResponse1 = ProductMapper.toResponseDto(
                ProductFixture.createProduct("product1", category)
        );
        ProductResponse productResponse2 = ProductMapper.toResponseDto(
                ProductFixture.createProduct("product2", category)
        );
        products.add(productResponse1);
        products.add(productResponse2);

        Page<ProductResponse> response = new PageImpl<>(products);
        String responseJson = objectMapper.writeValueAsString(response);
        given(productService.getPagedProducts(any()))
                .willReturn(response);

        mockMvc.perform(get("/api/products")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(productService).getPagedProducts(any());
    }

    @Test
    @DisplayName("상품 상세 조회 기능 테스트")
    void getProduct() throws Exception {
        ProductResponse response = ProductMapper.toResponseDto(
                ProductFixture.createProduct("product", category)
        );
        Long responseId = 1L;
        String responseJson = objectMapper.writeValueAsString(response);
        given(productService.getProductByIdOrThrow(any()))
                .willReturn(response);

        mockMvc.perform(get("/api/products/{id}", responseId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(productService).getProductByIdOrThrow(responseId);
    }

    @Test
    @DisplayName("상품 상세 조회 실패 테스트")
    void getProductFailed() throws Exception {
        Long productId = 1L;
        Throwable exception = new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        given(productService.getProductByIdOrThrow(productId))
                .willThrow(exception);

        mockMvc.perform(get("/api/products/{id}", productId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isNotFound())
                .andExpect(content().string(exception.getMessage()));

        verify(productService).getProductByIdOrThrow(productId);
    }

    @Test
    @DisplayName("상품 추가 기능 테스트")
    void addProduct() throws Exception {
        ProductRequest request = new ProductRequest(
                "product1",
                1000,
                "https://testshop.com",
                category.getName(),
                optionRequest);
        ProductResponse response = ProductMapper.toResponseDto(
                ProductMapper.toEntity(request, category)
        );
        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(response);
        given(productService.createProduct(any(ProductRequest.class)))
                .willReturn(response);

        mockMvc.perform(post("/api/products")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(productService).createProduct(request);
    }

    @Test
    @DisplayName("단일 상품 삭제 기능 테스트")
    void deleteProduct() throws Exception {
        Long productId = 1L;

        mockMvc.perform(delete("/api/products/{id}", productId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk());

        verify(productService).deleteProductById(productId);
    }

    @Test
    @DisplayName("상품 전체 삭제 기능 테스트")
    void deleteAllProducts() throws Exception {
        mockMvc.perform(delete("/api/products")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk());

        verify(productService).deleteAllProducts();
    }

    @Test
    @DisplayName("상품 수정 기능 테스트")
    void updateProduct() throws Exception {
        Long productId = 2L;
        ProductRequest request = new ProductRequest(
                "product2",
                3000,
                "https://testshop.com",
                category.getName(),
                optionRequest);
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/products/{id}", productId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(productService).updateProduct(productId, request);
    }

    @Test
    @DisplayName("상품 옵션 조회 테스트")
    void getProductOptions() throws Exception {
        Long productId = 1L;
        Set<OptionResponse> options = new HashSet<>();
        options.add(new OptionResponse(1L, "옵션1", 10));
        options.add(new OptionResponse(2L, "옵션2", 20));
        String responseJson = objectMapper.writeValueAsString(options);
        given(optionService.getProductOptionsByIdOrThrow(anyLong()))
                .willReturn(options);

        mockMvc.perform(get("/api/products/{id}/options", productId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(optionService).getProductOptionsByIdOrThrow(productId);
    }

    @Test
    @DisplayName("상품 옵션 추가 테스트")
    void addOptionToProduct() throws Exception {
        Long productId = 1L;
        OptionResponse response = new OptionResponse(1L, optionRequest.name(), optionRequest.quantity());
        String requestJson = objectMapper.writeValueAsString(optionRequest);
        String responseJson = objectMapper.writeValueAsString(response);
        given(optionService.addOptionToProduct(anyLong(), any()))
                .willReturn(response);

        mockMvc.perform(post("/api/products/{id}/options", productId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(optionService).addOptionToProduct(productId, optionRequest);
    }

    @Test
    @DisplayName("상품 옵션 삭제 테스트")
    void deleteOptionFromProduct() throws Exception {
        Long productId = 1L;
        String requestJson = objectMapper.writeValueAsString(optionRequest);

        mockMvc.perform(delete("/api/products/{id}/options", productId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(optionService).deleteOptionFromProduct(productId, optionRequest);
    }

}