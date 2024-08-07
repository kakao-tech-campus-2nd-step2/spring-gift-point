package gift.repository;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void notUniqueOptionNameExceptionTest_sameProduct() {
        // given
        Category category = new Category(1L, "test_뷰티");
        categoryRepository.save(category);

        Product product1 = new Product("product1", 500, "image");
        product1.setCategory(category);
        productRepository.save(product1);

        Option option1 = new Option("sameName", 100, product1);
        optionRepository.save(option1);
        entityManager.flush();
        entityManager.clear();

        Option option2 = new Option("sameName", 200, product1);

        // when
        // then
        assertThrows(DataIntegrityViolationException.class, () -> {
            optionRepository.saveAndFlush(option2);
        });
    }

    @Test
    @Transactional
    void notUniqueOptionNameExceptionTest_diffProduct() {
        // given
        Category category = new Category(1L, "test_뷰티");
        categoryRepository.save(category);

        Product product1 = new Product("product1", 500, "image");
        Product product2 = new Product("product2", 500, "image");
        product1.setCategory(category);
        product2.setCategory(category);
        productRepository.save(product1);
        productRepository.save(product2);

        Option option1 = new Option("sameName", 100, product1);
        optionRepository.save(option1);
        entityManager.flush();
        entityManager.clear();

        Option option2 = new Option("sameName", 200, product2);

        // when
        // then
        assertThrows(DataIntegrityViolationException.class, () -> {
            optionRepository.saveAndFlush(option2);
        });
    }

}