package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.Category;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 저장 및 ID로 조회")
    public void testSaveAndFindById() {
        Category category = new Category("Test Category", "#000000", "imageUrl", "description");
        Category savedCategory = categoryRepository.save(category);
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Test Category");
    }

    @Test
    @DisplayName("모든 카테고리 조회")
    public void testFindAll() {
        long initialCount = categoryRepository.count();

        Category category1 = new Category("Category 1", "#000000", "imageUrl", "description");
        Category category2 = new Category("Category 2", "#000000", "imageUrl", "description");

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        Iterable<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize((int) initialCount + 2);
    }

    @Test
    @DisplayName("카테고리 삭제")
    public void testDelete() {
        Category category = new Category("Test Category", "#000000", "imageUrl", "description");
        Category savedCategory = categoryRepository.save(category);

        categoryRepository.deleteById(savedCategory.getId());
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        assertThat(foundCategory).isNotPresent();
    }
}
