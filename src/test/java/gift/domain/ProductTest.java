package gift.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.domain.category.entity.Category;
import gift.domain.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    @DisplayName("상품 updateAll 테스트")
    void productUpdateAllTest() {
        // given
        Category category = new Category(1L, "name", "color", "image", "description");
        Product product = new Product("name", 1000, "image", category);
        // when
        product.updateAll("update", 1000, "image", category);

        // then
        assertAll(
            () -> assertThat(product.getName()).isEqualTo("update"),
            () -> assertThat(product.getPrice()).isEqualTo(1000),
            () -> assertThat(product.getImageUrl()).isEqualTo("image"),
            () -> assertThat(product.getCategory()).isEqualTo(category)
        );
    }
}
