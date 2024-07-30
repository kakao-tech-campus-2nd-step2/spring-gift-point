package gift.repositoryTest;

import gift.model.Category;
import gift.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindCategory() {
        Category category = new Category("Category1");
        categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findByName("Category1");
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Category1");
    }

    @Test
    void testDuplicateCategoryName() {
        Category category1 = new Category("같은 카테고리");
        categoryRepository.save(category1);

        Category category2 = new Category("같은 카테고리");

        assertThrows(DataIntegrityViolationException.class, () -> {
            categoryRepository.save(category2);
        });
    }

    @Test
    void testFindById() {
        Category category = new Category("Category2");
        categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Category2");
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category("Category3");
        categoryRepository.save(category);

        categoryRepository.deleteById(category.getId());

        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
        assertThat(foundCategory).isNotPresent();
    }
}
