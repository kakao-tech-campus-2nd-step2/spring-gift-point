package gift.api.option.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;

import gift.api.option.exception.InvalidSubtractionException;
import gift.api.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OptionTest {

    @Test
    @DisplayName("정상_옵션_수량_차감_테스트")
    void subtract() {
        // given
        var before = 100;
        var quantity = 57;
        var product = mock(Product.class);
        var option = new Option(product, "name", before);

        // when
        option.subtract(quantity);

        // then
        assertThat(option.getQuantity())
            .isEqualTo(before - quantity);
    }

    @Test
    @DisplayName("옵션_수량_차감_예외_테스트")
    void subtractException() {
        // given
        var product = mock(Product.class);
        var option = new Option(product, "name", 50);

        // when
        // then
        assertThatExceptionOfType(InvalidSubtractionException.class)
            .isThrownBy(() -> option.subtract(100));
    }
}