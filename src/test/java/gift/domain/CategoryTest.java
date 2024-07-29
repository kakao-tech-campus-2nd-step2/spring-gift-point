package gift.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.domain.category.dto.CategoryRequest;
import gift.domain.category.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    @DisplayName("카테고리 updateAll 테스트")
    void categoryUpdateAllTest() {
        // given
        CategoryRequest request = new CategoryRequest("update", "color", "image", "description");
        Category category = new Category(1L, "name", "color", "image", "description");

        // when
        category.updateAll(request.getName(), request.getColor(), request.getImageUrl(),
            request.getDescription());

        // then
        assertAll(
            () -> assertThat(category.getName()).isEqualTo(request.getName()),
            () -> assertThat(category.getColor()).isEqualTo(request.getColor()),
            () -> assertThat(category.getImageUrl()).isEqualTo(request.getImageUrl()),
            () -> assertThat(category.getDescription()).isEqualTo(request.getDescription())
        );
    }
}
