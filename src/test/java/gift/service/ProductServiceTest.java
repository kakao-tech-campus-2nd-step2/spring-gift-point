package gift.service;

import gift.dto.ProductDTO;
import gift.entity.CategoryEntity;
import gift.entity.ProductEntity;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryEntity categoryEntity;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        categoryEntity = new CategoryEntity("Test Category", "#FF0000", "http://imageUrl/1.png", "description");
        categoryEntity = categoryRepository.save(categoryEntity);

        productEntity = new ProductEntity("Test Product", 1000, "http://imageUrl/1.png", categoryEntity, new ArrayList<>());
        productEntity = productRepository.save(productEntity);
    }

    @Test
    void testGetAllProducts() {
        // When
        List<ProductDTO> products = productService.getAllProducts();

        // Then
        assertEquals(1, products.size());
        assertEquals(productEntity.getName(), products.get(0).getName());
    }


    @Test
    void testGetProduct() {
        // When
        Optional<ProductDTO> product = productService.getProduct(productEntity.getId());

        // Then
        assertTrue(product.isPresent());
        assertEquals(productEntity.getName(), product.get().getName());
    }

    @Test
    void testCreateProduct() {
        // Given
        ProductDTO newProductDTO = new ProductDTO(null, "New Product", 2000, "http://imagUrl/1", categoryEntity.getId(), null);

        // When
        productService.createProduct(newProductDTO);

        // Then
        List<ProductEntity> products = productRepository.findByName("New Product");
        assertFalse(products.isEmpty(), "새로 생성된 상품이 존재하지 않습니다.");
        ProductEntity newProductEntity = products.get(0);
        assertEquals("New Product", newProductEntity.getName());
    }

    @Test
    void testEditProduct() {
        // Given
        ProductDTO updatedProductDTO = new ProductDTO(productEntity.getId(), "Updated Product", 1500, "http://imagUrl/1", categoryEntity.getId(), null);

        // When
        productService.editProduct(productEntity.getId(), updatedProductDTO);

        // Then
        ProductEntity updatedProductEntity = productRepository.findById(productEntity.getId()).orElseThrow();
        assertEquals("Updated Product", updatedProductEntity.getName());
    }

    @Test
    void testDeleteProduct() {
        // When
        productService.deleteProduct(productEntity.getId());

        // Then
        assertThrows(EntityNotFoundException.class, () -> {
            productService.getProduct(productEntity.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        });
    }

    @Test
    void testCreateProductCategoryNotFound() {
        // Given
        ProductDTO newProductDTO = new ProductDTO(null, "product", 2000, "http://imagUrl/1", 999L, null);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> productService.createProduct(newProductDTO));
    }

    @Test
    void testEditProductCategoryNotFound() {
        // Given
        ProductDTO updatedProductDTO = new ProductDTO(productEntity.getId(), "product", 1500, "http://imagUrl/1", 999L, null);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> productService.editProduct(productEntity.getId(), updatedProductDTO));
    }
}