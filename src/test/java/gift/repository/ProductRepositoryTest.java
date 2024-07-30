package gift.repository;

import gift.entity.Category;
import gift.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateAndFindProduct() {
        // 새로운 카테고리 생성 및 저장
        Category category = new Category("Test Category", "#000000", "https://category.img", "detail");
        categoryRepository.save(category);

        // 새로운 제품 생성 및 저장
        Product product = new Product("Test Product", 1000, "https://product.img", category);
        productRepository.save(product);

        // 이름으로 제품 찾기
        Optional<Product> found = productRepository.findById(product.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Product");
        assertThat(found.get().getPrice()).isEqualTo(1000);
        assertThat(found.get().getImgUrl()).isEqualTo("https://product.img");
        assertThat(found.get().getCategory().getName()).isEqualTo("Test Category");

        // 제품 삭제
        productRepository.delete(found.get());

        // 삭제된 제품 확인
        Optional<Product> deleted = productRepository.findById(product.getId());
        assertThat(deleted).isNotPresent();
    }

    @Test
    public void testProductWithoutCategory() {
        // 카테고리 없이 제품 생성 시도
        Product product = new Product("Test Product", 1000, "https://product.img", null);

        // 카테고리가 없는 제품을 저장할 때 예외가 발생하는지 확인
        assertThatThrownBy(() -> productRepository.save(product))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}