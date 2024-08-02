package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Test Category", "Test Description", "Test Color", "Test Image URL");
        categoryRepository.save(category);
    }

    @Test
    @DisplayName("아이디로 찾기 테스트 (카테고리와 옵션 포함)")
    void findByIdWithCategoryAndOption() {
        Product product = new Product("kakao", 1000, "img", category);
        Option option = new Option("Test Option", 5);
        product.addOption(option);
        Product savedProduct = productRepository.save(product);

        Optional<Product> found = productRepository.findByIdWithCategoryAndOption(savedProduct.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(savedProduct.getId());
        assertThat(found.get().getName()).isEqualTo("kakao");
        assertThat(found.get().getPrice()).isEqualTo(1000);
        assertThat(found.get().getImageUrl()).isEqualTo("img");
        assertThat(found.get().getCategory()).isNotNull();
        assertThat(found.get().getOptions()).hasSize(1);
    }

    @Test
    @DisplayName("전체 상품 테스트 (카테고리와 옵션 포함)")
    void findAllWithCategoryAndOption() {
        Product product1 = new Product("kakao", 1000, "img1", category);
        Product product2 = new Product("pnu", 2000, "img2", category);
        Product product3 = new Product("uni", 3000, "img3", category);

        product1.addOption(new Option("Option 1", 5));
        product2.addOption(new Option("Option 2", 10));
        product3.addOption(new Option("Option 3", 15));

        productRepository.saveAll(Arrays.asList(product1, product2, product3));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> all = productRepository.findAllWithCategoryAndOption(pageable);

        assertThat(all).isNotNull();
        assertThat(all.getContent()).hasSize(3);
        assertThat(all.getContent()).extracting(Product::getName)
            .containsExactlyInAnyOrder("kakao", "pnu", "uni");
        assertThat(all.getContent()).extracting(Product::getPrice)
            .containsExactlyInAnyOrder(1000, 2000, 3000);
        assertThat(all.getContent()).extracting(Product::getImageUrl)
            .containsExactlyInAnyOrder("img1", "img2", "img3");
        assertThat(all.getContent()).allSatisfy(product -> {
            assertThat(product.getCategory()).isNotNull();
            assertThat(product.getOptions()).hasSize(1);
        });
        assertThat(all.getTotalElements()).isEqualTo(3);
        assertThat(all.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("아이디로 찾기 테스트 (옵션만 포함)")
    void findByIdWithOption() {
        Product product = new Product("kakao", 1000, "img", category);
        Option option = new Option("Test Option", 5);
        product.addOption(option);
        Product savedProduct = productRepository.save(product);

        Optional<Product> found = productRepository.findByIdWithOption(savedProduct.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(savedProduct.getId());
        assertThat(found.get().getName()).isEqualTo("kakao");
        assertThat(found.get().getPrice()).isEqualTo(1000);
        assertThat(found.get().getImageUrl()).isEqualTo("img");
        assertThat(found.get().getOptions()).hasSize(1);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 찾기 테스트")
    void findByNonExistentId() {
        Optional<Product> byId = productRepository.findById(999L);
        assertThat(byId).isEmpty();
    }

    @Test
    @DisplayName("제품 업데이트 테스트")
    void updateProduct() {
        Product product = new Product("original", 1000, "img", category);
        Product savedProduct = productRepository.save(product);

        savedProduct.updateProduct("updated", 2000, "newImg");
        productRepository.save(savedProduct);

        Product updatedProduct = productRepository.findById(savedProduct.getId()).orElseThrow();
        assertThat(updatedProduct.getName()).isEqualTo("updated");
        assertThat(updatedProduct.getPrice()).isEqualTo(2000);
        assertThat(updatedProduct.getImageUrl()).isEqualTo("newImg");
    }

    @Test
    @DisplayName("제품 삭제 테스트")
    void deleteProduct() {
        Product product = new Product("toDelete", 1000, "img", category);
        Product savedProduct = productRepository.save(product);

        productRepository.deleteById(savedProduct.getId());

        Optional<Product> deletedProduct = productRepository.findById(savedProduct.getId());
        assertThat(deletedProduct).isEmpty();
    }
}
