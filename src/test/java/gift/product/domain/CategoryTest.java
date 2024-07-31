package gift.product.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    @DisplayName("Category 객체 수정 테스트")
    void modifyCategoryTest() {
        // given
        Category category = new Category("카테고리", "색상", "이미지URL", "설명");
        String newName = "수정된 카테고리";
        String newColor = "수정된 색상";
        String newImgUrl = "수정된 이미지URL";
        String newDescription = "수정된 설명";
        // when
        category.modify(newName, newColor, newImgUrl, newDescription);

        // then
        assertThat(category.getName()).isEqualTo("수정된 카테고리");
        assertThat(category.getColor()).isEqualTo("수정된 색상");
        assertThat(category.getImgUrl()).isEqualTo("수정된 이미지URL");
        assertThat(category.getDescription()).isEqualTo("수정된 설명");
    }
}