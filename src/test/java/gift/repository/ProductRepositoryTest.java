package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gift.entity.Category;
import gift.entity.Product;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        Category category = categoryRepository.save(
            new Category("교환권", "#6c95d1", "image.jpg", "None"));
        Product expected = new Product("gift", 1000, "image.jpg", category);
        Product actual = productRepository.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @Test
    @DisplayName("DB에 저장된 ID를 기반으로 저장된 객체를 불러오는지 테스트")
    void findByIdTest() {
        Long First_Product_id = 1L;
        Optional<Product> actual = productRepository.findById(First_Product_id);

        assertThat(actual).isPresent().hasValueSatisfying(
            w -> assertThat(w.getName()).isEqualTo("Product 1"));
    }

    @Test
    @DisplayName("Valid 조건에 맞지 않는 이름이 들어갔을 경우 오류를 던지는지 테스트")
    void edgeCaseTest() {
        Category category = categoryRepository.save(
            new Category("교환권", "#6c95d1", "image.jpg", "None"));
        Product product = new Product("아이스 아메리카노 엑스라지 사이즈", 2000, "image.jpg", category);

        assertThatThrownBy(() -> {
            productRepository.save(product);
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("페이지네이션 테스트")
    void paginationTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = productRepository.findAll(pageable);

        assertThat(page.getTotalElements()).isEqualTo(15);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(10);

        assertThat(page.getContent().get(0).getName()).isEqualTo("Product 1");
    }
}
