package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.domain.Category;
import gift.domain.Product;
import gift.repository.fixture.CategoryFixture;
import gift.repository.fixture.ProductFixture;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("Create Test")
    void save() {
        // given
        Category category = CategoryFixture.createCategory("categoryName","color","description","url");
        categoryRepository.save(category);
        Product expected = ProductFixture.createProduct("test",100,"test.url",category);

        // when
        Product actual = productRepository.save(expected);
        em.flush();
        em.clear();

        // then
        assertAll(
            ()->assertThat(actual.getId()).isNotNull(),
            ()->assertThat(actual.getName()).isEqualTo(expected.getName()),
            ()->assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
            ()->assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }

    @Test
    @DisplayName("Read By Id Test")
    void findById() {
        // given
        Category category = CategoryFixture.createCategory("categoryName","color","description","url");
        categoryRepository.save(category);
        Product expected = ProductFixture.createProduct("test",100,"test.url",category);
        productRepository.save(expected);
        em.flush();
        em.clear();
        // when
        Product actual = productRepository.findById(expected.getId()).get();

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }

    @Test
    @DisplayName("Read All Test")
    void findAll() {
        // given
        Category category = CategoryFixture.createCategory("categoryName","color","description","url");
        categoryRepository.save(category);
        productRepository.save(ProductFixture.createProduct("test1",100,"test1.url",category));
        productRepository.save(ProductFixture.createProduct("test2",200,"test2.url",category));
        em.flush();
        em.clear();

        // when
        List<Product> actuals = productRepository.findAll();

        // then
        assertAll(
            () -> assertThat(actuals.size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("Delete By Id Test")
    void deleteById() {
        // given
        Category category = CategoryFixture.createCategory("categoryName","color","description","url");
        categoryRepository.save(category);
        Product expected = ProductFixture.createProduct("test",100,"test.url",category);
        productRepository.save(expected);
        em.flush();
        em.clear();
        // when
        productRepository.deleteById(expected.getId());
        Optional<Product> actual = productRepository.findById(expected.getId());
        // then
        assertThat(actual).isEmpty();
    }

}