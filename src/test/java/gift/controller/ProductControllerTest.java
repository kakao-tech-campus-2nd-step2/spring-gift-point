package gift.controller;

import gift.entity.Product;
import gift.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("전체 상품 조회")
    public void getAllProducts() throws Exception {
        Page<Product> products = new PageImpl<>(Arrays.asList(
                new Product(1L, "Product1", 100, "url1"),
                new Product(2L, "Product2", 200, "url2")
        ), PageRequest.of(0, 10), 2);
        given(productService.findAll(any(PageRequest.class))).willReturn(products);

        mockMvc.perform(get("/api/products?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Product1"))
                .andExpect(jsonPath("$.content[1].name").value("Product2"));
    }

    @Test
    @DisplayName("상품 추가")
    public void addProduct() throws Exception {
        Product product = new Product(1L, "Product1", 100, "url1");
        given(productService.save(any(Product.class))).willReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"price\":100,\"name\":\"Product1\",\"imageUrl\":\"url1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product1"));
    }

    @Test
    @DisplayName("상품 수정")
    public void updateProduct() throws Exception {
        Product product = new Product(1L, "Product1", 100, "url1");
        given(productService.save(any(Product.class))).willReturn(product);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"price\":100,\"name\":\"Product1\",\"imageUrl\":\"url1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product1"));
    }

    @Test
    @DisplayName("상품 삭제")
    public void deleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }
}
