package gift.repository;

import gift.vo.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Save Category")
    void save() {
        // given
        Category category = new Category(null, "new_카테고리명", "#123412", "http://testimage.png", "this is test");

        // when
        Category actual = categoryRepository.save(category);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @DisplayName("Find Category By Id")
    void findById() {
        // given
        Category category = new Category(null, "카테고리명", "#123412", "http://testimage.png", "this is test");
        categoryRepository.save(category);

        // when
        Category findedCategory = categoryRepository.findById(category.getId()).orElse(null);

        // then
        assertThat(findedCategory).isNotNull();
        assertThat(findedCategory.getId()).isEqualTo(category.getId());
    }

    @Test
    @DisplayName("Delete Category By Id")
    void deleteById() {
        // given
        Category category = new Category(null, "카테고리명", "#123412", "http://testimage.png", "this is test");
        Category savedCategory = categoryRepository.save(category);
        Long categoryId = savedCategory.getId();

        // when
        categoryRepository.deleteById(categoryId);

        // then
        assertThat(categoryRepository.findById(categoryId)).isEmpty();
    }

}