package gift.service;

import gift.model.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import gift.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("모든 제품 조회")
    void testGetAllProducts() {
        Product product1 = new Product("Product1", 100, "http://example.com/image1");
        Product product2 = new Product("Product2", 200, "http://example.com/image2");
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    @DisplayName("ID로 특정 제품 조회")
    void testGetProductById() {
        Product product = new Product("Product1", 100, "http://example.com/image1");
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);
        assertNotNull(foundProduct);
        assertEquals("Product1", foundProduct.getName());
        assertEquals(100, foundProduct.getPrice());
        assertEquals("http://example.com/image1", foundProduct.getImageUrl());
    }

    @Test
    @DisplayName("ID로 특정 제품 조회 - 존재하지 않는 경우")
    void testGetProductById_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
            () -> productService.getProductById(999L));

        assertEquals("Product not found with id: 999", exception.getMessage());
    }

    @Test
    @DisplayName("새로운 상품 생성")
    void testCreateProduct() {
        Product product = new Product("Product1", 100, "http://example.com/image1");
        when(productRepository.save(any(Product.class))).thenReturn(product);

        boolean success = productService.createProduct(product);
        assertTrue(success);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("기존 상품을 수정")
    void testUpdateProduct() {
        Product existingProduct = new Product("Product1", 100, "http://example.com/image1");
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product updatedProduct = new Product("UpdatedProduct1", 150, "http://example.com/updated_image1");
        boolean success = productService.updateProduct(1L, updatedProduct);

        assertTrue(success);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    @DisplayName("기존 상품을 부분 수정")
    void testPatchProduct() {
        Product existingProduct = new Product("Product1", 100, "http://example.com/image1");
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "UpdatedProduct1");
        updates.put("price", 150);

        boolean success = productService.patchProduct(1L, updates);

        assertTrue(success);
        assertEquals("UpdatedProduct1", existingProduct.getName());
        assertEquals(150, existingProduct.getPrice());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    @DisplayName("여러 기존 상품을 부분 수정")
    void testPatchProducts() {
        Product product1 = new Product("Product1", 100, "http://example.com/image1");
        Product product2 = new Product("Product2", 200, "http://example.com/image2");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(productRepository.save(any(Product.class))).thenReturn(product1).thenReturn(product2);

        Map<String, Object> updates1 = new HashMap<>();
        updates1.put("id", 1L);
        updates1.put("name", "UpdatedProduct1");
        updates1.put("price", 150);

        Map<String, Object> updates2 = new HashMap<>();
        updates2.put("id", 2L);
        updates2.put("name", "UpdatedProduct2");
        updates2.put("price", 250);

        List<Map<String, Object>> updatesList = Arrays.asList(updates1, updates2);

        List<Product> updatedProducts = productService.patchProducts(updatesList);

        assertEquals(2, updatedProducts.size());
        assertEquals("UpdatedProduct1", product1.getName());
        assertEquals(150, product1.getPrice());
        assertEquals("UpdatedProduct2", product2.getName());
        assertEquals(250, product2.getPrice());
        verify(productRepository, times(1)).save(product1);
        verify(productRepository, times(1)).save(product2);
    }

    @Test
    @DisplayName("기존 제품 삭제")
    void testDeleteProduct() {
        when(productRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(productRepository).deleteById(anyLong());

        boolean success = productService.deleteProduct(1L);
        assertTrue(success);
        verify(productRepository, times(1)).deleteById(1L);
    }
}