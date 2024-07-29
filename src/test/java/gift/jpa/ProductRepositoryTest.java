package gift.jpa;

import gift.category.Category;
import gift.category.CategoryRepository;
import gift.product.Product;
import gift.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    void save() {
        Category category = categoryRepository.findById(1L).orElseThrow();
        Product expected = new Product("상품1", 10000L, "상품1.jpg", category);
        Product actual = productRepository.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }

    @Test
    @DisplayName("ID로 상품 조회 테스트")
    void findByID() {
        Category category = categoryRepository.findById(1L).orElseThrow();
        Product expected = new Product("상품1", 10000L, "상품1.jpg", category);
        productRepository.save(expected);

        Product actual = productRepository.findById(expected.getId()).orElseThrow();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("ID로 상품 조회 실패 테스트")
    void findByIDFail() {
        Category category = categoryRepository.findById(1L).orElseThrow();
        Product expected = new Product("상품1", 10000L, "상품1.jpg", category);
        productRepository.save(expected);

        Optional<Product> actual = productRepository.findById(expected.getId() + 1);
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("상품 수정 테스트")
    void update() {
        Category category = categoryRepository.findById(1L).orElseThrow();
        Product expected = new Product("상품1", 10000L, "상품1.jpg", category);
        productRepository.save(expected);
        Optional<Product> product = productRepository.findById(expected.getId());
        product.ifPresent(product1 -> {
                    product1.setName("상품2");
                    product1.setPrice(20000L);
                    product1.setName("상품2.jpg");
                }
        );
        Product actual = productRepository.findById(expected.getId()).orElseThrow();

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void deleteByID() {
        Category category = categoryRepository.findById(1L).orElseThrow();
        Product product = new Product("상품1", 10000L, "상품1.jpg", category);
        productRepository.save(product);
        productRepository.deleteById(product.getId());
        Optional<Product> actual = productRepository.findById(product.getId());
        assertThat(actual.isPresent()).isFalse();
    }
}
