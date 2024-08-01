package gift.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "교환권");
    }

    @Test
    void testCreateValidCategory() {
        assertAll(
            () -> assertThat(category.getId()).isNotNull(),
            () -> assertThat(category.getName()).isEqualTo("교환권")
        );
    }

    @Test
    void testCreateWithNullName() {
        try {
            Category category = new Category(1L, null);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWithEmptyName() {
        try {
            Category category = new Category(1L, "");
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateValidName() {
        category.updateName("테스트");
        assertThat("테스트").isEqualTo(category.getName());
    }

    @Test
    void testUpdateWithNullName() {
        try {
            category.updateName(null);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithEmptyName() {
        try {
            category.updateName("");
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

}