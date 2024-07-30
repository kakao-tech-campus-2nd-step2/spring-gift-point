package gift.product.repository;

import gift.member.domain.*;
import gift.product.domain.ImageUrl;
import gift.product.domain.Product;
import gift.product.domain.ProductName;
import gift.product.domain.ProductPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    private Product product;
    private Product savedProduct;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void beforeEach() {
        product = new Product(null, new ProductName("name"), new ProductPrice(10L), new ImageUrl("imageUrl"));
        savedProduct = productRepository.save(product);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @Description("save 테스트")
    void saveTest() {
        // given
        // when
        // then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(savedProduct.getImageUrl()).isEqualTo(product.getImageUrl());
    }

    @Test
    @Description("findById 테스트")
    void findByIdTest() {
        // given
        // when
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        // then
        assertThat(foundProduct).contains(product);
    }

    @Test
    @Description("findAll 테스트")
    void findAll() {
        // given
        Product product1 = new Product(null, new ProductName("name1"), new ProductPrice(10L), new ImageUrl("imageUrl1"));
        Product product2 = new Product(null, new ProductName("name2"), new ProductPrice(10L), new ImageUrl("imageUrl2"));
        product1 = productRepository.save(product1);
        product2 = productRepository.save(product2);
        entityManager.flush();

        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).contains(product1, product2);
    }

    @Test
    @Description("update 테스트")
    void updateTest() {
        // given
        // when
        savedProduct = new Product(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice(), savedProduct.getImageUrl());
        Product updateProduct = productRepository.save(savedProduct);
        entityManager.flush();

        // then
        assertThat(updateProduct).isEqualTo(savedProduct);
    }

    @Test
    @Description("deleteById 테스트")
    void deleteTest() {
        // given
        // when
        productRepository.deleteById(savedProduct.getId());
        Optional<Product> products = productRepository.findById(product.getId());

        // then
        assertThat(products).isEmpty();
    }
}