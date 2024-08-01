package gift.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Test
    void categoryConstructorAndGetters() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");

        assertThat(category.getName()).isEqualTo("Test Category");
        assertThat(category.getColor()).isEqualTo("#FFFFFF");
        assertThat(category.getImageUrl()).isEqualTo("http://example.com/cat.jpg");
        assertThat(category.getDescription()).isEqualTo("Description");
    }

    @Test
    void categorySetters() {
        Category category = new Category();
        category.setName("New Category");
        category.setColor("#000000");
        category.setImageUrl("http://example.com/new-cat.jpg");
        category.setDescription("New Description");

        assertThat(category.getName()).isEqualTo("New Category");
        assertThat(category.getColor()).isEqualTo("#000000");
        assertThat(category.getImageUrl()).isEqualTo("http://example.com/new-cat.jpg");
        assertThat(category.getDescription()).isEqualTo("New Description");
    }
}
