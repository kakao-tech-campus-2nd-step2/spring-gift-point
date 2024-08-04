package gift.service;

import gift.dto.category.CategoryRequest;
import gift.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
public class CategoryServiceTest {

    private Category category;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        category = categoryService.save(new CategoryRequest("test", "#test", "", ""));
    }

    @Test
    void readCategory() {
        // given
        // when
        Category expect = categoryService.findById(category.getId());

        // then
        assertThat(expect.getId()).isEqualTo(category.getId());
    }

    @Test
    void updateCategory() {
        // given
        // when
        CategoryRequest update = new CategoryRequest("updated_category", "#updated_color", "updated_url", "updated_desc");
        Category expect = categoryService.update(category.getId(), update);

        // then
        assertAll(
                () -> assertThat(expect.getName()).isEqualTo(update.getName()),
                () -> assertThat(expect.getColor()).isEqualTo(update.getColor()),
                () -> assertThat(expect.getImageUrl()).isEqualTo(update.getImage_url()),
                () -> assertThat(expect.getDescription()).isEqualTo(update.getDescription())
        );
    }


    @Test
    @DisplayName("상품에 매핑된 카테고리가 삭제될 시 해당 상품의 category가 [DefaultCategory]로 리턴됨")
    void deleteCategory() {
        // given
        // when
        categoryService.delete(category.getId());

        // then
        Category expect = categoryService.findById(category.getId());
        assertThat(expect.getId()).isEqualTo(-1L);
    }
}
