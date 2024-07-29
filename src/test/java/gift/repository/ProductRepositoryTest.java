package gift.repository;

import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Category;
import gift.entity.Product;
import jakarta.transaction.Transactional;

@DataJpaTest
public class ProductRepositoryTest {
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product expected;
    private Product actual;
    private Product updateProduct;
    private long notExistId;
    private Category category;

    @BeforeEach
    void setUp(){
        category = new Category("test", "color", "imageUrl", "");
        categoryRepository.save(category);
        expected = new Product("testName", 1, "testUrl", category);
        actual = productRepository.save(expected);
        updateProduct = new Product("updateName", 1, "testUrl", category);
        notExistId = 1000L;
    }

    @Transactional
    @Test
    void save() {
    
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Transactional
    @Test
    void findById() {

        Optional<Product> product = productRepository.findById(expected.getId());
        Optional<Product> notExistProduct = productRepository.findById(notExistId);

        assertAll(
            () -> assertThat(product).isPresent(),
            () -> assertThat(product.get().getId()).isEqualTo(expected.getId()),
            () -> assertThat(notExistProduct).isNotPresent()
        );
    }

    @Transactional
    @Test
    void update() {

        Product product = productRepository.findById(expected.getId()).orElseThrow();
        productRepository.delete(product);
        productRepository.save(updateProduct);

        Optional<Product> updatedProduct = productRepository.findById(updateProduct.getId());

        assertAll(
            () -> assertThat(updatedProduct).isPresent(),
            () -> assertThat(updatedProduct.get().getName()).isEqualTo("updateName")
        );
    }

    @Transactional
    @Test
    void delete(){
        
        productRepository.delete(expected);

        Optional<Product> product = productRepository.findById(expected.getId());
        assertThat(product).isNotPresent();
    }

}
