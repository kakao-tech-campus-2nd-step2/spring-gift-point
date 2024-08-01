package gift.entity;

import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    public void setUp() {
        testCategory = new Category( "test", "test", "test", "test");
        categoryRepository.save(testCategory);

        testProduct = new Product(testCategory,1, "test", "testURL");
        productRepository.save(testProduct);
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void testFindById() {
        categoryRepository.save(testCategory);
        Product foundProduct = productRepository.findById(testProduct.getId());
        assertEquals(testProduct.getId(), foundProduct.getId());
    }

    @Test
    void testSaveProduct() {
        Product savedProduct = productRepository.save(testProduct);
        assertEquals(testProduct.getId(), savedProduct.getId());
    }

    @Test
    void testUpdateProduct() {
        categoryRepository.save(testCategory);
        Product updatedProduct = new Product(1, testCategory,1, "updated", "testURL");
        Product savedProduct = productRepository.save(updatedProduct);
        assertEquals("updated", savedProduct.getName());
    }
}
