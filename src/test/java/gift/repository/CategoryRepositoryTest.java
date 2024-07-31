package gift.repository;

import gift.entity.Category;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(CategoryRepositoryTest.TestConfig.class)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Validator validator;

    @SpringJUnitConfig
    static class TestConfig {
        @Bean
        public Validator validator() {
            return Validation.buildDefaultValidatorFactory().getValidator();
        }
    }

    @Test
    void testSaveAndFindCategoryByName() {
        Category category = new Category("Unique Category", "#000000", "http://category.jpg", "detail");
        categoryRepository.save(category);

        Category foundCategory = categoryRepository.findByName("Unique Category");
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getName()).isEqualTo("Unique Category");
    }

    @Test
    void testDeleteCategoryByName() {
        Category category = new Category("Category to Delete", "#000000", "http://category.jpg", "detail");
        categoryRepository.save(category);

        categoryRepository.delete(category);

        Category deletedCategory = categoryRepository.findByName("Category to Delete");
        assertThat(deletedCategory).isNull();
    }

    @Test
    void testSaveCategoryWithoutNameShouldThrowException() {
        Category categoryWithoutName = new Category(null, "#000000", "http://category.jpg", "detail");

        Set<ConstraintViolation<Category>> violations = validator.validate(categoryWithoutName);
        assertThat(violations).isNotEmpty();

        assertThatThrownBy(() -> categoryRepository.save(categoryWithoutName))
                .isInstanceOf(ConstraintViolationException.class);
    }
}