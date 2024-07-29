package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.domain.category.entity.Category;
import gift.domain.category.repository.CategoryRepository;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.sql.init.mode=never"
})
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("findById 테스트")
    void findByIdTest() {
        // given
        Category savedCategory = categoryRepository.save(createCategory());
        Product expected = productRepository.save(createProduct(savedCategory));

        // when
        Product actual = productRepository.findById(expected.getId()).orElseThrow();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        // given
        Category savedCategory = categoryRepository.save(createCategory());
        Product expected = createProduct(savedCategory);

        // when
        Product actual = productRepository.save(expected);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl()),
            () -> assertThat(actual.getCategory().getId()).isEqualTo(expected.getCategory().getId())
        );
    }

    @Test
    @DisplayName("delete 테스트")
    void deleteTest() {
        // given
        Category savedCategory = categoryRepository.save(createCategory());
        Product savedProduct = productRepository.save(createProduct(savedCategory));

        // when
        productRepository.delete(savedProduct);

        // then
        assertTrue(productRepository.findById(savedProduct.getId()).isEmpty());
    }

    @Test
    @DisplayName("카테고리로 상품 전체 조회 테스트")
    void findAllByCategoryTest() {
        // when
        Category category = createCategory();
        Product product1 = createProduct(category);
        Product product2 = createProduct(category);

        categoryRepository.save(category);
        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> expected = Arrays.asList(product1, product2);

        // when
        List<Product> actual = productRepository.findAllByCategory(category);

        // then
        assertAll(
            () -> IntStream.range(0, actual.size()).forEach(i -> {
                assertThat(actual.get(i).getName())
                    .isEqualTo(expected.get(i).getName());
                assertThat(actual.get(i).getPrice())
                    .isEqualTo(expected.get(i).getPrice());
                assertThat(actual.get(i).getImageUrl())
                    .isEqualTo(expected.get(i).getImageUrl());
                assertThat(actual.get(i).getCategory())
                    .isEqualTo(expected.get(i).getCategory());
            })
        );
    }

    private Category createCategory() {
        return new Category("test", "color", "image", "description");
    }

    private Product createProduct(Category category) {
        return new Product("test", 1000, "test.jpg", category);
    }

}