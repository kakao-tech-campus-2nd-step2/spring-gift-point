package gift;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gift.model.Option;
import gift.model.Product;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductTest {

    @Test
    public void testUpdate() {
        // given
        List<Option> options = new ArrayList<>();
        Product product = new Product(1L, "Original Name", 100, "http://original.image.url", 1L, options);

        // when
        product.update("Updated Name", 200, "http://updated.image.url", 2L);

        // then
        assertAll(
            () -> assertEquals(1L, product.getId()),
            () -> assertEquals("Updated Name", product.getName()),
            () -> assertEquals(200, product.getPrice()),
            () -> assertEquals("http://updated.image.url", product.getImageUrl()),
            () -> assertEquals(2L, product.getCategoryId())
        );
    }
}