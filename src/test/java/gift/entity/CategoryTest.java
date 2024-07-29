package gift.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("음식", "testColor", "testImage.jpg", "TestDescription");
    }

    @Test
    @DisplayName("생성자 테스트")
    void CategoryConstructorTest() {
        Category newCategory = new Category("상품권", "testColor2", "testImage2.jpg",
            "TestDescription2");

        assertThat(newCategory).isNotNull();
        assertThat(newCategory.getName()).isEqualTo("상품권");
        assertThat(newCategory.getColor()).isEqualTo("testColor2");
        assertThat(newCategory.getImageUrl()).isEqualTo("testImage2.jpg");
        assertThat(newCategory.getDescription()).isEqualTo("TestDescription2");
    }

    @Test
    @DisplayName("Getter 테스트")
    void CategoryGetterTest() {
        assertThat(category.getName()).isEqualTo("음식");
        assertThat(category.getColor()).isEqualTo("testColor");
        assertThat(category.getImageUrl()).isEqualTo("testImage.jpg");
        assertThat(category.getDescription()).isEqualTo("TestDescription");
    }

    @Test
    @DisplayName("updateCategory 테스트")
    void updateCategoryTest() {
        category.updateCategory("자동차", "자동차 색상", "자동차 이미지", "자동차 카테고리입니다.");

        assertThat(category.getName()).isEqualTo("자동차");
        assertThat(category.getColor()).isEqualTo("자동차 색상");
        assertThat(category.getImageUrl()).isEqualTo("자동차 이미지");
        assertThat(category.getDescription()).isEqualTo("자동차 카테고리입니다.");
    }

    @Test
    @DisplayName("emptyCategoryCheck 테스트")
    void emptyCategoryCheckTest() {
        assertThat(category.emptyCategoryCheck()).isTrue();
    }
}
