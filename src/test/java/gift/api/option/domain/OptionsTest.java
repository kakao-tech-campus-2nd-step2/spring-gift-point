package gift.api.option.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;

import gift.api.option.exception.InvalidNameException;
import gift.api.product.domain.Product;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OptionsTest {

    @Test
    @DisplayName("중복된_이름의_옵션_추가_테스트")
    void validateUniqueName() {
        // given
        var product = mock(Product.class);
        var name = "option";
        var quantity = 500;
        Option option = new Option(product, name, quantity);
        Options options = new Options(List.of(option));

        // when
        // then
        assertThatExceptionOfType(InvalidNameException.class)
            .isThrownBy(() -> options.validateUniqueName(new Option(product, name, quantity)));
    }
}