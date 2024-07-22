package gift.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.product.persistence.entity.Category;
import gift.product.persistence.entity.Product;
import gift.product.persistence.repository.CategoryJpaRepository;
import gift.product.persistence.repository.ProductJpaRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
public class ProductJpaRepositoryTest {
    @Autowired
    private ProductJpaRepository productRepository;

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
    }

    private static Category category;

    @BeforeAll
    static void setUp(@Autowired CategoryJpaRepository categoryRepository) {
        category = categoryRepository.save(new Category("name"));
    }

    @Test
    void save() {
        // given
        Product expected = new Product("product", "description", 1000, "http://url.com", category);

        // when
        Product actual = productRepository.save(expected);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    void findById() {
        // given
        Product expected = new Product("product", "description", 1000, "http://url.com", category);
        Product saved = productRepository.save(expected);

        // when
        Product actual = productRepository.findById(saved.getId()).orElse(null);

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(saved.getId())
        );
    }

    @Test
    void existsById() {
        // given
        Product expected = new Product("product", "description", 1000, "http://url.com", category);
        Product saved = productRepository.save(expected);

        // when
        boolean actual = productRepository.existsById(saved.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void deleteById() {
        // given
        Product expected = new Product("product", "description", 1000, "http://url.com", category);
        Product saved = productRepository.save(expected);

        // when
        productRepository.deleteById(saved.getId());

        // then
        assertThat(productRepository.existsById(saved.getId())).isFalse();
    }

    @Test
    void findAll() {
        // given
        Product product1 = new Product("product1", "description1", 1000, "http://url1.com", category);
        Product product2 = new Product("product2", "description2", 2000, "http://url2.com", category);
        productRepository.save(product1);
        productRepository.save(product2);

        // when
        Iterable<Product> actual = productRepository.findAll();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void deleteAllById() {
        // given
        Product product1 = new Product("product1", "description1", 1000, "http://url1.com", category);
        Product product2 = new Product("product2", "description2", 2000, "http://url2.com", category);
        Long id1 = productRepository.save(product1).getId();
        Long id2 = productRepository.save(product2).getId();

        // when
        productRepository.deleteAllById(List.of(id1, id2));

        // then
        assertThat(productRepository.findAll()).isEmpty();
    }

    @Test
    void findAllById() {
        // given
        Product product1 = new Product("product1", "description1", 1000, "http://url1.com", category);
        Product product2 = new Product("product2", "description2", 2000, "http://url2.com", category);
        Long id1 = productRepository.save(product1).getId();
        Long id2 = productRepository.save(product2).getId();

        // when
        List<Product> actual = productRepository.findAllById(List.of(id1, id2));

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void findAll_Pageable() {
        // given
        Product product1 = new Product("product1", "description1", 1000, "http://url1.com", category);
        Product product2 = new Product("product2", "description2", 2000, "http://url2.com", category);
        productRepository.save(product1);
        productRepository.save(product2);
        PageRequest pageRequest = PageRequest.of(0, 1);

        // when
        Page<Product> actual = productRepository.findAll(pageRequest);

        // then
        assertThat(actual).hasSize(1);
        assertThat(actual.hasNext()).isTrue();
    }

}
