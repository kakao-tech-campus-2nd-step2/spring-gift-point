package gift.category.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import gift.category.CategoryFixture;
import gift.category.entity.Category;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("카테고리 리파지토리 테스트")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        // 임의의 카테고리 3개
        categoryRepository.saveAll(List.of(
                CategoryFixture.createCategory("카테고리1", "#000000", "category1.png", "카테고리1입니다."),
                CategoryFixture.createCategory("카테고리2", "#111111", "category2.png", "카테고리2입니다."),
                CategoryFixture.createCategory("카테고리3", "#222222", "category3.png", "카테고리3입니다.")
        ));
    }

    @Test
    @DisplayName("카테고리 생성")
    void addCategory() {
        //given
        Category category = CategoryFixture.createCategory("카테고리4", "#333333", "category4.png", "카테고리4입니다.");

        //when
        Category savedCategory = categoryRepository.save(category);

        //then
        assertThat(savedCategory)
                .hasFieldOrPropertyWithValue("name", category.getName())
                .hasFieldOrPropertyWithValue("color", category.getColor())
                .hasFieldOrPropertyWithValue("imageUrl", category.getImageUrl())
                .hasFieldOrPropertyWithValue("description", category.getDescription());
    }

    @Test
    @DisplayName("카테고리 조회")
    void findCategory() {
        //given
        Category category = CategoryFixture.createCategory("카테고리5", "#444444", "category5.png", "카테고리5입니다.");
        categoryRepository.save(category);

        //when
        List<Category> categories = categoryRepository.findAll();
        Category savedCategory = categories.getLast();

        //then
        assertThat(savedCategory)
                .hasFieldOrPropertyWithValue("name", category.getName())
                .hasFieldOrPropertyWithValue("color", category.getColor())
                .hasFieldOrPropertyWithValue("imageUrl", category.getImageUrl())
                .hasFieldOrPropertyWithValue("description", category.getDescription());
    }

    @Test
    @DisplayName("카테고리 수정")
    void updateCategory() {
        //given
        Category category = CategoryFixture.createCategory("카테고리6", "#555555", "category6.png", "카테고리6입니다.");
        Category savedCategory = categoryRepository.save(category);

        //when
        savedCategory.update("카테고리7", "#666666", "category7.png", "카테고리7입니다.");

        Category updatedCategory = categoryRepository.findById(savedCategory.getId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );

        //then
        assertThat(updatedCategory)
                .hasFieldOrPropertyWithValue("name", "카테고리7")
                .hasFieldOrPropertyWithValue("color", "#666666")
                .hasFieldOrPropertyWithValue("imageUrl", "category7.png")
                .hasFieldOrPropertyWithValue("description", "카테고리7입니다.");
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategory() {
        //given
        Category category = CategoryFixture.createCategory("카테고리8");
        Category savedCategory = categoryRepository.save(category);

        //when
        categoryRepository.delete(savedCategory);

        //then
        assertFalse(categoryRepository.findById(savedCategory.getId()).isPresent());
    }
}
