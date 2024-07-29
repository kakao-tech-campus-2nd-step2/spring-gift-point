package gift.controller;

import static gift.util.constants.ProductConstants.INVALID_PRICE;
import static gift.util.constants.ProductConstants.PRODUCT_NOT_FOUND;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductResponse;
import gift.dto.product.ProductUpdateRequest;
import gift.exception.product.InvalidProductPriceException;
import gift.exception.product.ProductNotFoundException;
import gift.service.ProductService;
import gift.util.TokenValidator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private TokenValidator tokenValidator;

    private ProductResponse productResponse;

    @BeforeEach
    public void setUp() {
        productResponse = new ProductResponse(
            1L,
            "Test Product",
            100,
            "test.jpg",
            1L,
            "categoryName"
        );
    }


    @Test
    @DisplayName("모든 상품 조회 (페이지네이션 적용)")
    public void testGetAllProducts() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductResponse> page = new PageImpl<>(List.of(productResponse), pageable, 1);

        when(productService.getAllProducts(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("Test Product"))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @DisplayName("상품 ID로 조회")
    public void testGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productResponse);

        mockMvc.perform(get("/api/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 조회")
    public void testGetProductByIdNotFound() throws Exception {
        when(productService.getProductById(1L)).thenThrow(
            new ProductNotFoundException(PRODUCT_NOT_FOUND + 1));

        mockMvc.perform(get("/api/products/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value(PRODUCT_NOT_FOUND + 1));
    }

    @Test
    @DisplayName("상품 추가")
    public void testAddProduct() throws Exception {
        when(productService.addProduct(any(ProductCreateRequest.class))).thenReturn(
            productResponse);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\": \"Test Product\", \"price\": 100, \"imageUrl\": \"test.jpg\", \"categoryId\": 1}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @DisplayName("유효하지 않은 가격으로 상품 추가")
    public void testAddProductInvalidPrice() throws Exception {
        when(productService.addProduct(any(ProductCreateRequest.class))).thenThrow(
            new InvalidProductPriceException(INVALID_PRICE));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\": \"Test Product\", \"price\": -100, \"imageUrl\": \"test.jpg\", \"categoryId\": 1}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value(INVALID_PRICE));
    }

    @Test
    @DisplayName("상품 업데이트")
    public void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductUpdateRequest.class))).thenReturn(
            productResponse);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\": \"Updated Product\", \"price\": 200, \"imageUrl\": \"updated.jpg\", \"categoryId\": 1}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 업데이트")
    public void testUpdateProductNotFound() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductUpdateRequest.class))).thenThrow(
            new ProductNotFoundException(PRODUCT_NOT_FOUND + 1));

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\": \"Updated Product\", \"price\": 200, \"imageUrl\": \"updated.jpg\", \"categoryId\": 1}"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value(PRODUCT_NOT_FOUND + 1));
    }

    @Test
    @DisplayName("상품 삭제")
    public void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 삭제")
    public void testDeleteProductNotFound() throws Exception {
        doNothing().when(productService).deleteProduct(1L);
        doThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND + 1)).when(productService)
            .deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value(PRODUCT_NOT_FOUND + 1));
    }
}
