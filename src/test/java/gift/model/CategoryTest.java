package gift.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.exception.InvalidInputValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "교환권", "#007700", "예시 이미지", "예시 설명");
    }

    @Test
    void testCreateValidCategory() {
        assertAll(
            () -> assertThat(category.getId()).isNotNull(),
            () -> assertThat(category.getName()).isEqualTo("교환권"),
            () -> assertThat(category.getColor()).isEqualTo("#007700"),
            () -> assertThat(category.getImageUrl()).isEqualTo("예시 이미지"),
            () -> assertThat(category.getDescription()).isEqualTo("예시 설명")
        );
    }

    @Test
    void testCreateWithNullName() {
        try {
            Category category = new Category(1L, null, "#007700", "예시 이미지", "예시 설명");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithEmptyName() {
        try {
            Category category = new Category(1L, "", "#007700", "예시 이미지", "예시 설명");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithNullColor() {
        try {
            Category category = new Category(1L, "교환권", null, "예시 이미지", "예시 설명");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithEmptyColor() {
        try {
            Category category = new Category(1L, "교환권", "", "예시 이미지", "예시 설명");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithNullImageUrl() {
        try {
            Category category = new Category(1L, "교환권", "#007700", null, "예시 설명");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithEmptyImageUrl() {
        try {
            Category category = new Category(1L, "교환권", "#007700", "", "예시 설명");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateValidValue() {
        category.updateCategory("테스트", "#770077", "테스트 이미지", "테스트 설명");
        assertAll(
            () ->assertThat("테스트").isEqualTo(category.getName()),
            () -> assertThat("#770077").isEqualTo(category.getColor()),
            () -> assertThat("테스트 이미지").isEqualTo(category.getImageUrl()),
            () -> assertThat("테스트 설명").isEqualTo(category.getDescription())
        );
    }

    @Test
    void testUpdateWithNullName() {
        try {
            category.updateCategory(null, "#770077", "테스트 이미지", "테스트 설명");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithEmptyName() {
        try {
            category.updateCategory("", "#770077", "테스트 이미지", "테스트 설명");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithNullColor() {
        try {
            category.updateCategory("테스트", null, "테스트 이미지", "테스트 설명");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithEmptyColor() {
        try {
            category.updateCategory("테스트", "", "테스트 이미지", "테스트 설명");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithNullImageUrl() {
        try {
            category.updateCategory("테스트", "#770077", null, "테스트 설명");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithEmptyImageUrl() {
        try {
            category.updateCategory("테스트", "#770077", "", "테스트 설명");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }


}