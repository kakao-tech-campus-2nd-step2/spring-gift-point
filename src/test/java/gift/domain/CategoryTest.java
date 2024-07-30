package gift.domain;

import gift.model.category.Category;
import gift.model.category.CategoryRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    @DisplayName("카테고리 생성 테스트")
    void create() {
        // given
        String name = "상품권";
        String color = "black";
        String imageUrl = "image";
        String description = "";

        // when
        Category category = createCategory(name, color, imageUrl, description);

        // then
        Assertions.assertThat(category.getName()).isEqualTo(name);
        Assertions.assertThat(category.getColor()).isEqualTo(color);
        Assertions.assertThat(category.getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("카테고리 수정 기능 테스트")
    void updateCategory() {
        // given
        Category category = createCategory("상품권", "black", "image", "");
        String newName = "디지털";
        String newColor = "white";
        String newImageUrl = "image";
        String newDescription = "";
        CategoryRequest request = new CategoryRequest(newName, newColor, newImageUrl, newDescription);

        // when
        category.update(request);

        // then
        Assertions.assertThat(category.getName()).isEqualTo(newName);
        Assertions.assertThat(category.getColor()).isEqualTo(newColor);
    }

    private Category createCategory(String name, String color, String imageUrl, String description) {
        return new Category(name, color, imageUrl, description);
    }

}
