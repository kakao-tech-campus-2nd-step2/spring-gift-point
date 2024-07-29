package gift.repository;

import gift.model.category.Category;
import gift.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리가 잘 저장되는지 확인")
    void saveCategory() {
        //given
        Category category = new Category(null, "test1", "test", "test", "test");

        //when
        Category savedCategory = categoryRepository.save(category);

        //then
        assertThat(savedCategory).isNotNull();
        assertThat(category).isSameAs(savedCategory);
        assertThat(savedCategory.getId()).isEqualTo(category.getId());
    }

    @Test
    @DisplayName("카테고리가 잘 조회되는지 확인")
    void findCategory() {
        //given
        Category savedCategory1 = categoryRepository.save(new Category(null, "test1", "test", "test", "test"));
        Category savedCategory2 = categoryRepository.save(new Category(null, "test2", "test", "test", "test"));

        //when
        Category findCategory1 = categoryRepository.findById(savedCategory1.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 카테고리가 없습니다. id : " + savedCategory1.getId()));
        Category findCategory2 = categoryRepository.findById(savedCategory2.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 카테고리가 없습니다. id : " + savedCategory2.getId()));

        //then
        assertThat(findCategory1.getId()).isEqualTo(savedCategory1.getId());
        assertThat(findCategory2.getId()).isEqualTo(savedCategory2.getId());
    }

    @Test
    @DisplayName("카테고리가 잘 수정되는지 확인")
    void updateCategory() {
        //given
        Category savedCategory = categoryRepository.save(new Category(null, "test1", "test", "test", "test"));

        //when
        savedCategory.modify("updateTest", "updateTest", "updateTest", "updateTest");
        Category updatedCategory = categoryRepository.save(savedCategory);

        //then
        assertThat(updatedCategory.getName()).isEqualTo("updateTest");
    }

    @Test
    @DisplayName("카테고리가 잘 삭제되었는지 확인")
    void deleteCategory() {
        //given
        Category savedCategory = categoryRepository.save(new Category(null, "test1", "test", "test", "test"));

        //when
        categoryRepository.delete(savedCategory);
        Optional<Category> deletedCategory = categoryRepository.findById(savedCategory.getId());

        //then
        assertThat(deletedCategory).isNotPresent();
    }
}