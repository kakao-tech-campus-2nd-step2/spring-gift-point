package gift.product.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.product.model.Category;
import gift.product.model.Product;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Product product;

    @BeforeEach
    public void setUp() {
        Category category = categoryRepository.save(new Category("교환권"));
        product = new Product(
                "상품1",
                1500,
                "product1.images",
            category
        );
    }

    @Test
    void testRegisterProduct() {
        Product registerProduct = productRepository.save(product);
        assertThat(registerProduct.getId()).isNotNull();
        assertThat(registerProduct.getName()).isEqualTo("상품1");
        assertThat(registerProduct.getPrice()).isEqualTo(1500);
        assertThat(registerProduct.getImageUrl()).isEqualTo("product1.images");
    }

    @Test
    void testSearchProductById() {
        Product savedProduct = productRepository.save(product);

        Optional<Product> retrievedProduct = productRepository.findById(savedProduct.getId());
        System.out.println("삽입된 객체의 ID = " + savedProduct.getId());

        assertThat(retrievedProduct).isPresent();
        assertAll(
            () -> assertThat(retrievedProduct.get().getId()).isEqualTo(savedProduct.getId()),
            () -> assertThat(retrievedProduct.get().getName()).isEqualTo("상품1"),
            () -> assertThat(retrievedProduct.get().getPrice()).isEqualTo(1500),
            () -> assertThat(retrievedProduct.get().getImageUrl()).isEqualTo("product1.images")
        );
    }

    @Test
    void testUpdateProduct() {
        Product registerProduct = productRepository.save(product);

        productRepository.save(
            new Product(
                    registerProduct.getId(),
                    registerProduct.getName(),
                    2000,
                    registerProduct.getImageUrl(),
                    registerProduct.getCategory()
            )
        );

        Optional<Product> updateProduct = productRepository.findById(registerProduct.getId());

        assertThat(updateProduct).isPresent();
        assertThat(updateProduct.get().getPrice()).isEqualTo(2000);
    }

    @Test
    void testDeleteProduct() {
        Product savedProduct = productRepository.save(product);

        Optional<Product> retrievedProduct = productRepository.findById(savedProduct.getId());
        assertThat(retrievedProduct).isPresent();

        productRepository.deleteById(savedProduct.getId());

        Optional<Product> deletedProduct = productRepository.findById(savedProduct.getId());
        assertThat(deletedProduct).isNotPresent();
    }
}
