package gift.controller;

import gift.model.Product;
import gift.exception.ProductNotFoundException;
import gift.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("모든 제품 조회")
    void testGetAllProducts() {
        Product product1 = new Product("Product1", 100, "http://example.com/image1");
        Product product2 = new Product("Product2", 200, "http://example.com/image2");
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        ResponseEntity<Map<String, Object>> response = productController.getAllProducts();
        Map<String, Object> responseBody = response.getBody();

        assertNotNull(responseBody);
        assertEquals(2, ((List<Product>) responseBody.get("products")).size());
        assertEquals("All products retrieved successfully.", responseBody.get("message"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("ID로 특정 제품 조회")
    void testGetProductById() {
        Product product = new Product("Product1", 100, "http://example.com/image1");
        when(productService.getProductById(anyLong())).thenReturn(product);

        ResponseEntity<Map<String, Object>> response = productController.getProductById(1L);
        Map<String, Object> responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("Product retrieved successfully.", responseBody.get("message"));
        assertEquals(product, responseBody.get("product"));
    }

    @Test
    @DisplayName("ID로 특정 제품 조회 - 존재하지 않는 경우")
    void testGetProductById_NotFound() {
        when(productService.getProductById(anyLong())).thenThrow(new ProductNotFoundException("Product not found with id: 999"));

        ResponseEntity<Map<String, Object>> response = productController.getProductById(999L);
        Map<String, Object> responseBody = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("Product not found with id: 999", responseBody.get("message"));
    }

    @Test
    @DisplayName("새로운 상품 생성")
    void testCreateProduct() {
        Product product = new Product("Product1", 100, "http://example.com/image1");
        when(productService.createProduct(any(Product.class))).thenReturn(true);

        ResponseEntity<Map<String, Object>> response = productController.createProduct(product);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Product created successfully.", response.getBody().get("message"));
        assertEquals(product, response.getBody().get("product"));
    }

    @Test
    @DisplayName("기존 상품을 수정")
    void testUpdateProduct() {
        Product updatedProduct = new Product("UpdatedProduct1", 150, "http://example.com/updated_image1");
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(true);
        when(productService.getProductById(anyLong())).thenReturn(updatedProduct);

        ResponseEntity<Map<String, Object>> response = productController.updateProduct(1L, updatedProduct);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Product updated successfully.", response.getBody().get("message"));
        assertEquals(updatedProduct, response.getBody().get("product"));
    }

    @Test
    @DisplayName("기존 상품을 부분 수정")
    void testPatchProduct() {
        Product updatedProduct = new Product("UpdatedProduct1", 150, "http://example.com/updated_image1");
        when(productService.patchProduct(anyLong(), any(Map.class))).thenReturn(true);
        when(productService.getProductById(anyLong())).thenReturn(updatedProduct);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "UpdatedProduct1");
        updates.put("price", 150);

        ResponseEntity<Map<String, Object>> response = productController.patchProduct(1L, updates);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Product patched successfully.", response.getBody().get("message"));
        assertEquals(updatedProduct, response.getBody().get("product"));
    }

    @Test
    @DisplayName("여러 기존 상품을 부분 수정")
    void testPatchProducts() {
        Product product1 = new Product("UpdatedProduct1", 150, "http://example.com/updated_image1");
        Product product2 = new Product("UpdatedProduct2", 250, "http://example.com/updated_image2");
        when(productService.patchProducts(anyList())).thenReturn(Arrays.asList(product1, product2));

        Map<String, Object> updates1 = new HashMap<>();
        updates1.put("id", 1L);
        updates1.put("name", "UpdatedProduct1");
        updates1.put("price", 150);

        Map<String, Object> updates2 = new HashMap<>();
        updates2.put("id", 2L);
        updates2.put("name", "UpdatedProduct2");
        updates2.put("price", 250);

        List<Map<String, Object>> updatesList = Arrays.asList(updates1, updates2);

        ResponseEntity<Map<String, Object>> response = productController.patchProducts(updatesList);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("All products patched successfully.", response.getBody().get("message"));
        List<Product> updatedProducts = (List<Product>) response.getBody().get("updatedProducts");
        assertEquals(2, updatedProducts.size());
        assertEquals(product1, updatedProducts.get(0));
        assertEquals(product2, updatedProducts.get(1));
    }

    @Test
    @DisplayName("기존 제품 삭제")
    void testDeleteProduct() {
        when(productService.deleteProduct(anyLong())).thenReturn(true);

        ResponseEntity<Map<String, Object>> response = productController.deleteProduct(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
