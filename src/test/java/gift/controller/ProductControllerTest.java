package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.ForbiddenWordException;
import gift.exception.ProductNotFoundException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.service.ProductService;
import gift.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;
    private Category testCategory;
    private Option option1;
    private Option option2;

    @BeforeEach
    public void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setColor("#FFFFFF");
        testCategory.setImageUrl("http://example.com/image.jpg");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(1000);
        testProduct.setImageUrl("http://example.com/product_image.jpg");
        testProduct.setCategory(testCategory);

        option1 = new Option();
        option1.setName("Option 1");
        option1.setQuantity(10);
        option1.setProduct(testProduct);

        option2 = new Option();
        option2.setName("Option 2");
        option2.setQuantity(20);
        option2.setProduct(testProduct);

        testProduct.setOptions(Arrays.asList(option1, option2));
    }

    @Test
    public void testGetAllProducts_Success() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = new PageImpl<>(List.of(testProduct), pageable, 1);

        when(productService.getProducts(any(Pageable.class), any())).thenReturn(products);

        mockMvc.perform(get("/api/products")
                .param("categoryId", "1")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "name,asc")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content[0].name").value(testProduct.getName()));
    }

    @Test
    public void testGetProductById_Success() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(testProduct);

        mockMvc.perform(get("/api/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.name").value(testProduct.getName()));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(anyLong())).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/api/products/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("Product not found"))
            .andExpect(jsonPath("$.errorCode").value("404"));
    }

    @Test
    public void testCreateProduct_Success() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(true);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.name").value(testProduct.getName()));
    }

    @Test
    public void testCreateProduct_ForbiddenWord() throws Exception {
        testProduct.setName("카카오 상품");
        when(productService.createProduct(any(Product.class)))
            .thenThrow(new ForbiddenWordException("상품 이름에 '카카오'가 포함된 경우 담당 MD와 협의가 필요합니다."));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
            .andExpect(status().isBadRequest())  // 400을 예상
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("상품 이름에 '카카오'가 포함된 경우 담당 MD와 협의가 필요합니다."))
            .andExpect(jsonPath("$.errorCode").value("400"));  // 올바른 경로 사용
    }

    @Test
    public void testUpdateProduct_Success() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(1200);
        updatedProduct.setImageUrl("http://example.com/updated_product_image.jpg");
        updatedProduct.setCategory(testCategory);
        updatedProduct.setOptions(Arrays.asList(option1, option2));

        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(true);
        when(productService.getProductById(anyLong())).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.name").value("Updated Product"));
    }

    @Test
    public void testUpdateProduct_NotFound() throws Exception {
        doThrow(new ProductNotFoundException("Product not found with id: 99"))
            .when(productService).updateProduct(eq(99L), any(Product.class));

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(1200);
        updatedProduct.setImageUrl("http://example.com/updated_product_image.jpg");
        updatedProduct.setCategory(testCategory);
        updatedProduct.setOptions(Arrays.asList(option1, option2));

        mockMvc.perform(put("/api/products/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("Product not found with id: 99"));
    }

    @Test
    public void testDeleteProduct_Success() throws Exception {
        when(productService.deleteProduct(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteProduct_NotFound() throws Exception {
        doThrow(new ProductNotFoundException("Product not found with id: 99"))
            .when(productService).deleteProduct(99L);

        mockMvc.perform(delete("/api/products/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("Product not found with id: 99"));
    }
}
