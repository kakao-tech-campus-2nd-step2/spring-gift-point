package gift.service;

import gift.domain.ProductDTO;
import gift.entity.Category;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProduct() {
        // Given
        int page = 0;
        int size = 10;
        Category testCategory = new Category("test", "test", "test", "test");
        List<Product> products = Arrays.asList(
                new Product(testCategory,1, "test", "testURL"),
                new Product(testCategory,1, "test", "testURL")
        );
        when(productRepository.findByProductIdBetween(anyInt(), anyInt())).thenReturn(products);

        // When
        Page<Product> result = productService.getAllProduct(page, size);

        // Then
        assertEquals(2, result.getContent().size());
        verify(productRepository).findByProductIdBetween(0, 10);
    }

    @Test
    void testGetProductById() {
        // Given
        int id = 1;
        Category testCategory = new Category("test", "test", "test", "test");
        Product product = new Product(testCategory,1, "test", "testURL");
        when(productRepository.findById(id)).thenReturn(product);

        // When
        Optional<Product> result = productService.getProductById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @Test
    void testGetProductByIdNotFound() {
        // Given
        int id = 1;
        when(productRepository.findById(id)).thenReturn(null);

        // When
        Optional<Product> result = productService.getProductById(id);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void testAddProduct() {
        // Given
        ProductDTO productDTO = new ProductDTO(1, 100, "Test Product", "image.jpg");
        Category testCategory = new Category("test", "test", "test", "test");
        Product product = new Product(testCategory, 100, "Test Product", "image.jpg");
        when(categoryRepository.findById(1)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        Product result = productService.addProduct(productDTO);

        // Then
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testAddProductCategoryNotFound() {
        // Given
        ProductDTO productDTO = new ProductDTO(1, 100, "Test Product", "image.jpg");
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> productService.addProduct(productDTO));
    }

    @Test
    void testUpdateProduct() {
        // Given
        int id = 1;
        ProductDTO productDTO = new ProductDTO(1, 100, "Updated Product", "image.jpg");
        Category testCategory = new Category("test", "test", "test", "test");
        Product updatedProduct = new Product(id, testCategory, 100, "Updated Product", "image.jpg");
        when(categoryRepository.findById(1)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // When
        Product result = productService.updateProduct(id, productDTO);

        // Then
        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        // Given
        int id = 1;
        doNothing().when(productRepository).deleteById(id);

        // When
        assertDoesNotThrow(() -> productService.deleteProduct(id));

        // Then
        verify(productRepository).deleteById(id);
    }

    @Test
    void testDeleteProductNotFound() {
        // Given
        int id = 1;
        doThrow(new RuntimeException()).when(productRepository).deleteById(id);

        // When & Then
        assertThrows(NoSuchElementException.class, () -> productService.deleteProduct(id));
    }
}