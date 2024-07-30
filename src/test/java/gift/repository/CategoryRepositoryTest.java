package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.model.category.Category;
import gift.repository.category.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 생성 테스트")
    void save(){
        // given
        Category category = createCategory("상품권", "black", "image", "");
        // when
        Category savedCategory = categoryRepository.save(category);
        // then
        Assertions.assertAll(
            () -> assertThat(savedCategory.getId()).isNotNull(),
            () -> assertThat(savedCategory.getName()).isEqualTo(category.getName())
        );
    }

    @Test
    @DisplayName("id에 따른 카테고리 찾기")
    void findCategoryById() {
        // given
        Category category = createCategory("상품권", "black", "image", "");
        categoryRepository.save(category);
        Long id = category.getId();
        // when
        Optional<Category> foundCategory = categoryRepository.findById(id);
        // then
        org.assertj.core.api.Assertions.assertThat(foundCategory).isNotNull();

    }

    @Test
    @DisplayName("카테고리 삭제 기능 테스트")
    void deleteByCategoryId() {
        // given
        Category category = createCategory("상품권", "black", "image", "");
        categoryRepository.save(category);
        Long deleteId = category.getId();
        // when
        categoryRepository.deleteById(deleteId);
        List<Category> savedCategory = categoryRepository.findAll();
        // then
        assertThat(savedCategory.size()).isNotNull();
    }

    private Category createCategory(String name, String color, String imageUrl, String description) {
        return new Category(name, color, imageUrl, description);
    }

}
