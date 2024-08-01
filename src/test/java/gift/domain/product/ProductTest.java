package gift.domain.product;

import gift.domain.category.Category;
import gift.domain.option.Option;
import gift.exception.CustomException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ProductTest {
    @Test
    void 상품_등록_실패_옵션이_없는_경우() {
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> new Product("상품", 1000,
                        "Test.jpg",
                        new Category("카테고리", "색깔", "카테고리", "test"),
                        List.of()));
    }

    @Test
    void 상품_등록_성공() {
        Product product = new Product("상품", 1000,
                "Test.jpg",
                new Category("카테고리", "색깔", "카테고리", "test"),
                List.of(new Option("옵션1", 100), new Option("옵션2", 100)));

        assertThat(product.getOptions().get(0).getName()).isEqualTo("옵션1");
        assertThat(product.getOptions().get(1).getName()).isEqualTo("옵션2");
    }

    @Test
    void 상품_등록_실패_중복된_이름() {
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> new Product("상품", 1000,
                        "Test.jpg",
                        new Category("카테고리", "색깔", "카테고리", "test"),
                        List.of(new Option("옵션1", 100), new Option("옵션1", 100))));
    }
}
