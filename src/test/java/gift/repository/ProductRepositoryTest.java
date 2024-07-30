package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.model.category.Category;
import gift.model.product.Product;
import gift.repository.category.CategoryRepository;
import gift.repository.product.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        Category category = createCategory("상품권", "black", "image", "");
        categoryRepository.save(category);
    }

    @DisplayName("상품 정보 저장 테스트")
    @Test
    void save() {
        // given
        Category category = categoryRepository.findAll().get(0);
        Product product1 = createProduct("americano", 4500, "americano", category);
        // when
        Product savedProduct = productRepository.save(product1);
        // then
        Assertions.assertAll(
            () -> assertThat(savedProduct.getId()).isNotNull(),
            () -> assertThat(savedProduct.getName()).isEqualTo(product1.getName())
        );
    }

    @DisplayName("id에 따른 상품 찾기 테스트")
    @Test
    void findById() {
        // given
        Category category = categoryRepository.findAll().get(0);
        Product product1 = createProduct("americano", 4500, "americano", category);
        productRepository.save(product1);
        Long id = product1.getId();
        // when
        Product foundProduct = productRepository.findById(id).orElse(null);
        // then
        assertThat(foundProduct).isNotNull();
    }

    @DisplayName("상품 삭제 기능 테스트")
    @Test
    void deleteById() {
        // given
        Category category = categoryRepository.findAll().get(0);
        Product product1 = createProduct("americano", 4500, "americano", category);
        productRepository.save(product1);
        Long DeleteId = product1.getId();

        // when
        productRepository.deleteById(DeleteId);
        List<Product> remainingProducts = productRepository.findAll();

        // then
        assertThat(remainingProducts.size()).isEqualTo(0);
    }

    private Product createProduct(String name, int price, String url, Category category) {
        return new Product(name, price, url, category);
    }
    private Category createCategory(String name, String color, String imageUrl, String description) {
        return new Category(name, color, imageUrl, description);
    }
}
