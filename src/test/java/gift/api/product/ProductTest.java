package gift.api.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.mock;

import gift.api.category.Category;
import gift.api.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductTest {

    @Test
    void update() {
        // given
        Category category = mock(Category.class);
        Product product = new Product(category, "hi", 10, "url");
        var name = "name";
        var price = 500;
        var imageUrl = "image";

        // when
        product.update(category, name, price, imageUrl);

        // then
        assertAll(
            () -> assertThat(product.getName()).isEqualTo(name),
            () -> assertThat(product.getPrice()).isEqualTo(price),
            () -> assertThat(product.getImageUrl()).isEqualTo(imageUrl)
        );
    }
}