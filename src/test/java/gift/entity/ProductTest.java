package gift.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void productConstructorAndGetters() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/test.jpg", "Description");
        Product product = new Product("Test Product", 100, "http://example.com/test.jpg", category);

        assertThat(product.getName()).isEqualTo("Test Product");
        assertThat(product.getPrice()).isEqualTo(100);
        assertThat(product.getImageUrl()).isEqualTo("http://example.com/test.jpg");
    }

    @Test
    void productSetters() {
        Product product = new Product();
        product.setName("New Product");
        product.setPrice(200);
        product.setImageUrl("http://example.com/new.jpg");

        assertThat(product.getName()).isEqualTo("New Product");
        assertThat(product.getPrice()).isEqualTo(200);
        assertThat(product.getImageUrl()).isEqualTo("http://example.com/new.jpg");
    }
}
