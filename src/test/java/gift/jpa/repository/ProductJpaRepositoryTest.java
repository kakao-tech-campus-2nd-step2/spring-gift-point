package gift.jpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.product.category.entity.Category;
import gift.product.category.repository.CategoryJpaRepository;
import gift.product.entity.Product;
import gift.product.repository.ProductJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = {"/sql/initialize.sql", "/sql/insert_categories.sql",
    "/sql/insert_products.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductJpaRepositoryTest {

    @Autowired
    private ProductJpaRepository productRepository;
    @Autowired
    private CategoryJpaRepository categoryRepository;

    @Test
    @DisplayName("find all test")
    void findAllTest() {
        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("find by id test")
    void findByIdTest() {
        // when
        final Product actual = productRepository.findById(1L).get();
        Product expected = new Product(
            "Product A",
            1000,
            "http://example.com/images/product_a.jpg",
            null
        );

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl());
    }

    @Test
    @DisplayName("find by not exist id test")
    void findByNotExistIdTest() {
        // when
        final Optional<Product> actual = productRepository.findById(10L);

        // then
        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("create test")
    void createTest() {
        // given
        Category category = categoryRepository.findById(1L).get();
        Product newProduct = new Product(
            "new product",
            10_000,
            "http://example.com/images/product_new.jpg",
            category
        );

        // when
        final Product actual = productRepository.save(newProduct);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("new product");
        assertThat(actual.getPrice()).isEqualTo(10_000);
        assertThat(actual.getImageUrl()).isEqualTo("http://example.com/images/product_new.jpg");
    }

    @Test
    @DisplayName("update test")
    void updateTest() {
        // given
        final Product updateProduct = productRepository.findById(1L)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // when
        updateProduct.changeName("update product");
        final Product actual = productRepository.save(updateProduct);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo("update product");
        assertThat(actual.getPrice()).isEqualTo(1_000);
        assertThat(actual.getImageUrl()).isEqualTo("http://example.com/images/product_a.jpg");
    }

    @Test
    @DisplayName("delete test")
    void deleteTest() {
        // when
        productRepository.deleteById(1L);
        List<Product> products = productRepository.findAll();
        Optional<Product> deletedProduct = productRepository.findById(1L);

        // then
        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(4);
        assertThat(deletedProduct).isNotPresent();
    }
}
