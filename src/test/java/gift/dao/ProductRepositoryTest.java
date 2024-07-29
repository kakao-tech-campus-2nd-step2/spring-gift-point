package gift.dao;

import gift.product.dto.OptionRequest;
import gift.product.entity.Category;
import gift.product.dao.ProductRepository;
import gift.product.dto.ProductRequest;
import gift.product.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import testFixtures.CategoryFixture;
import testFixtures.ProductFixture;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = CategoryFixture.createCategory("상품권");
    }

    @Test
    @DisplayName("상품 추가 및 ID 조회 테스트")
    void saveAndFindById() {
        Product product = ProductFixture.createProduct("product", category);
        Product savedProduct = productRepository.save(product);

        Product foundProduct = productRepository.findById(savedProduct.getId())
                .orElse(null);

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo(savedProduct.getName());
        assertThat(foundProduct.getPrice()).isEqualTo(savedProduct.getPrice());
        assertThat(foundProduct.getImageUrl()).isEqualTo(savedProduct.getImageUrl());
    }

    @Test
    @DisplayName("상품 ID 조회 실패 테스트")
    void findByIdFailed() {
        Product product = ProductFixture.createProduct("product", category);
        Product savedProduct = productRepository.save(product);

        Product foundProduct = productRepository.findById(savedProduct.getId() + 1L)
                .orElse(null);

        assertThat(foundProduct).isNull();
    }

    @Test
    @DisplayName("상품 ID 리스트 조회 테스트")
    void findByIds() {
        List<Product> products = new ArrayList<>();
        products.add(productRepository.save(
                ProductFixture.createProduct("product1", category)
        ));
        products.add(productRepository.save(
                ProductFixture.createProduct("product2", category)
        ));
        products.add(productRepository.save(
                ProductFixture.createProduct("product3", category)
        ));
        products.add(productRepository.save(
                ProductFixture.createProduct("product4", category)
        ));

        List<Product> foundProducts = productRepository.findAll();

        assertThat(foundProducts).containsAll(products);
    }

    @Test
    @DisplayName("상품 수정 테스트")
    void updateProduct() {
        Product product = ProductFixture.createProduct("product", category);
        ProductRequest request = new ProductRequest(
                "updateproduct",
                12345,
                "updateproduct.jpg",
                category.getName(),
                new OptionRequest("옵션", 10)
        );
        Product savedProduct = productRepository.save(product);
        savedProduct.update(request, category);

        Product foundProduct = productRepository.findById(savedProduct.getId())
                .orElse(null);
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo(savedProduct.getName());
        assertThat(foundProduct.getPrice()).isEqualTo(savedProduct.getPrice());
        assertThat(foundProduct.getImageUrl()).isEqualTo(savedProduct.getImageUrl());
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void deleteProduct() {
        Product product = ProductFixture.createProduct("product", category);
        Product savedProduct = productRepository.save(product);

        productRepository.deleteById(savedProduct.getId());

        boolean exists = productRepository.existsById(savedProduct.getId());
        assertThat(exists).isFalse();
    }

}