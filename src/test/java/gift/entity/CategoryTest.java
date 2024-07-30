package gift.entity;

import static org.assertj.core.api.Assertions.assertThat;

import gift.dto.CategoryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    @DisplayName("카테고리 업데이트 테스트")
    void updateCategory() {
        Category category = new Category("카카오프렌즈", "Yellow", "http", "카카오프렌즈 카테고리입니다.");
        CategoryDto updatedCategory = new CategoryDto(null, "카카오프렌즈 플러스", "노랑", "http",
            "카카오프렌즈 플러스 카테고리입니다.");
        category.updateCategory(updatedCategory);
        assertThat(category.getName()).isEqualTo(updatedCategory.getName());
        assertThat(category.getColor()).isEqualTo(updatedCategory.getColor());
    }
}