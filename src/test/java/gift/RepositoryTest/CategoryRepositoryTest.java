package gift.RepositoryTest;

import gift.domain.Category;
import gift.domain.Menu;
import gift.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 저장 테스트")
    void testSaveCategory() {
        Category category = new Category(null,"한식",new LinkedList<Menu>());
        category = categoryRepository.save(category);

        assertThat(category).isNotNull();
    }

    @Test
    @DisplayName("id로 카테고리 찾기 테스트")
    void testFindCategoryById() {
        Category category = new Category(null,"한식",new LinkedList<Menu>());
        category = categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getId()).isEqualTo(category.getId());
    }


    @Test
    @DisplayName("카테고리 삭제 테스트")
    void testDeleteCategory() {
        Category category = new Category(null,"한식",new LinkedList<Menu>());
        category = categoryRepository.save(category);

        categoryRepository.deleteById(category.getId());

        Optional<Category> deletedCategory = categoryRepository.findById(category.getId());
        assertThat(deletedCategory).isNotPresent();
    }

    @Test
    @DisplayName("카테고리 FindAll 테스트")
    void testFindAllCategories() {
        Category category1 = new Category(null,"한식",new LinkedList<Menu>());
        categoryRepository.save(category1);

        Category category2 = new Category(null,"중식",new LinkedList<Menu>());
        categoryRepository.save(category2);

        Iterable<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(2);
    }

    @Test
    @DisplayName("카테고리 동등성 테스트")
    void testEquals() {
        Category category1 = new Category(null,"한식",new LinkedList<Menu>());
        categoryRepository.save(category1);

        Category category2 = new Category(null,"한식",new LinkedList<Menu>());
        categoryRepository.save(category2);

        Set<Category> categories = new HashSet<>();
        categories.add(category1);
        categories.add(category2);

        assertThat(categories).hasSize(1);
    }
}
