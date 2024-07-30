package gift.repository;

import static gift.util.CategoryFixture.createCategory;
import static gift.util.OptionFixture.createOption;
import static gift.util.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gift.domain.Option;
import gift.domain.Product;
import gift.exception.InsufficientQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    private int quantity;
    private Option option;

    @BeforeEach
    void setup() {
        quantity = 3;
        Product product = createProduct(1L, "아이스 아메리카노", createCategory());
        option = optionRepository.save(createOption(null, "test", quantity, product));
        System.out.println(option.toDTO());
    }

    @DisplayName("옵션 수량 차감")
    @Test
    void subtract() {
        // given
        int subtractedQuantity = 1;

        // when
        option.subtract(subtractedQuantity);
        option = optionRepository.save(option);

        // then
        assertThat(option.getQuantity()).isEqualTo(quantity - subtractedQuantity);
    }

    @DisplayName("옵션 수량 초과 차감")
    @Test
    void subtractOverQuantity() {
        // given
        int subtractedQuantity = 4;

        // when
        // then
        assertThatExceptionOfType(InsufficientQuantityException.class)
            .isThrownBy(() -> option.subtract(subtractedQuantity));
    }
}
