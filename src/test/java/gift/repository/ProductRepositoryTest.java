package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.common.exception.ErrorCode;
import gift.common.exception.ProductException;
import gift.model.Category;
import gift.model.Product;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/truncate.sql")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("상품 등록")
    void save() {
        Category category = new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다.");
        categoryRepository.save(category);

        Product product = new Product(null, "상품1", 1000, "image1.jpg", category);
        Product actual = productRepository.save(product);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(product.getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(product.getPrice()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(product.getImageUrl())
        );
    }

    @Test
    @DisplayName("상품 조회")
    void findById() {
        Category category = new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다.");
        categoryRepository.save(category);

        Product product = productRepository.save(
            new Product(null, "상품1", 1000, "image1.jpg", category));

        Product actual = productRepository.findById(product.getId())
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        assertThat(actual).isEqualTo(product);
    }

    @Test
    @DisplayName("전체 상품 조회")
    void findAll() {
        Category category = new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다.");
        categoryRepository.save(category);

        productRepository.save(new Product(null, "상품1", 1000, "image1.jpg", category));
        productRepository.save(new Product(null, "상품2", 2000, "image2.jpg", category));

        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(2);
    }

    @Test
    @DisplayName("상품 수정")
    void update() {
        Category category = new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다.");
        categoryRepository.save(category);

        Product product = productRepository.save(
            new Product(null, "상품1", 1000, "image1.jpg", category));

        product.updateProduct("수정된 상품", 2000, "update.jpg");

        assertAll(
            () -> assertThat(product.getName()).isEqualTo("수정된 상품"),
            () -> assertThat(product.getPrice()).isEqualTo(2000),
            () -> assertThat(product.getImageUrl()).isEqualTo("update.jpg")
        );
    }

    @Test
    @DisplayName("상품 삭제")
    void delete() {
        Category category = new Category(null, "차량", "brown", "www.aaa.jpg", "차량 카테고리입니다.");
        categoryRepository.save(category);

        productRepository.save(new Product(null, "상품1", 1000, "image1.jpg", category));
        productRepository.save(new Product(null, "상품2", 2000, "image2.jpg", category));

        productRepository.deleteById(1L);
        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(1);
    }
}
