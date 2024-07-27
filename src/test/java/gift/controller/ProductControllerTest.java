package gift.controller;

import gift.entity.Category;
import gift.entity.Product;
import gift.exception.GlobalExceptionHandler;
import gift.service.ProductService;
import gift.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.persistence.EntityNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        Mockito.reset(productService, jwtUtil);
    }

    @Test
    @DisplayName("GET /api/products/{id} - Found")
    public void testGetProductById() throws Exception {
        Category category = new Category();
        category.update("Test Category", "Red", "https://example.com/image.jpg", "Category Description");
        Product product = new Product("Test Product", 1000, "https://example.com/image.jpg", category);
        product.updateId(1L);

        when(productService.findById(anyLong())).thenReturn(product);

        mockMvc.perform(get("/api/products/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @DisplayName("GET /api/products/{id} - Not Found")
    public void testGetProductByIdNotFound() throws Exception {
        when(productService.findById(anyLong())).thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(get("/api/products/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"));
    }

    @Test
    @DisplayName("POST /api/products - Add Product")
    public void testAddProduct() throws Exception {
        Category category = new Category();
        category.update("Test Category", "Red", "https://example.com/image.jpg", "Category Description");
        Product product = new Product("ValidProduct", 1000, "https://example.com/image.jpg", category);
        product.updateId(1L);

        when(productService.getCategoryById(anyLong())).thenReturn(category);
        when(productService.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ValidProduct\",\"price\":1000,\"imageUrl\":\"https://example.com/image.jpg\",\"categoryId\":1, \"options\":[{\"name\":\"Option1\",\"quantity\":10}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("ValidProduct"));
    }

    @Test
    @DisplayName("PUT /api/products/{id} - Update Product")
    public void testUpdateProduct() throws Exception {
        Category existingCategory = new Category();
        existingCategory.update("Test Category", "Blue", "https://example.com/existing_image.jpg", "Existing Description");
        Product existingProduct = new Product("Test Product", 1000, "https://example.com/existing_image.jpg", existingCategory);
        existingProduct.updateId(1L);

        Category updatedCategory = new Category();
        updatedCategory.update("updatedCategory", "Red", "https://example.com/updated_image.jpg", "Updated Description");
        Product updatedProduct = new Product("updatedProduct", 1500, "https://example.com/image_updated.jpg", updatedCategory);
        updatedProduct.updateId(1L);

        when(productService.findById(anyLong())).thenReturn(existingProduct);
        when(productService.getCategoryById(anyLong())).thenReturn(updatedCategory);
        when(productService.save(any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"updatedProduct\",\"price\":1500,\"imageUrl\":\"https://example.com/image_updated.jpg\",\"categoryId\":1, \"options\":[{\"name\":\"Option1\",\"quantity\":10}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("updatedProduct"));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - Delete Product")
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());
    }
}
