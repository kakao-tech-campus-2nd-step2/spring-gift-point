package gift.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Nested
    @DisplayName("Product 생성자는 ")
    class Describe_createProduct {
        @Test
        @DisplayName("상품 옵션이 없으면 예외를 발생시킨다.")
        void create_product_with_no_option() {
            assertThatThrownBy(() -> new Product.Builder()
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 옵션은 최소 1개 이상이어야 합니다.");
        }

        @Test
        @DisplayName("상품 옵션이 있으면 Product를 생성한다.")
        void create_product_with_option() {
            //given
            List<ProductOption> productOptions = List.of(new ProductOption.Builder().build());

            //when
            Product product = new Product.Builder()
                .productOptions(productOptions)
                .build();

            //then
            assertAll(
                () -> assertNotNull(product),
                () -> assertEquals(productOptions, product.getProductOptions())
            );
        }
    }
}