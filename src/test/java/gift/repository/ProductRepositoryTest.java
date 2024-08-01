package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private String name = "product";
    private int price = 10000;
    private String imageUrl = "image.jpg";

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void saveProductTest() {
        // Given
        var product = new Product(name, price, imageUrl);

        // When
        productRepository.save(product);

        // Then
        var savedProduct = productRepository.findAll().getFirst();
        assertThat(savedProduct).isEqualTo(product);
    }

    @Test
    void findProductByName() {
        // Given
        var product = new Product(name, price, imageUrl);
        productRepository.save(product);

        // When
        var foundProduct = productRepository.findByName(name);

        // Then
        assertTrue(foundProduct.isPresent());
        assertThat(foundProduct.get()).isEqualTo(product);
    }

    @Test
    void deleteProductByIdTest() {
        // Given
        var product1 = new Product("sampleName", 10000, "product.jpg");
        var product2 = new Product("sample2", 10200, "product2.jpg");
        productRepository.save(product1);
        productRepository.save(product2);

        // When
        productRepository.deleteById(product1.getId());

        // Then
        assertAll(
            () -> assertThat(productRepository.findById(product1.getId()).isPresent()).isFalse(),
            () -> assertThat(productRepository.findAll().isEmpty()).isFalse()
        );
    }
}