package gift.entity;

import gift.exception.InsufficientOptionQuantityException;
import gift.repository.OptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("옵션 엔티티 테스트")
class OptionTest {

    @Autowired
    OptionRepository optionRepository;

    @Test
    @DisplayName("Name Null")
    void nameNull() {
        Option option = new Option(null, 100);

        assertThatThrownBy(() -> optionRepository.save(option))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Quantity Null")
    void quantityNull() {
        Option option = new Option("name", null);

        assertThatThrownBy(() -> optionRepository.save(option))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("정상적으로")
    void success() {
        Option option = new Option("name", 100);

        assertThat(optionRepository.save(option).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("옵션 수량 차감 - 성공")
    void subtractSuccess() {
        //Given
        Option option = new Option("name", 100);

        //When
        option.subtract(50);

        //Then
        assertThat(option.getQuantity()).isEqualTo(50);
    }

    @Test
    @DisplayName("옵션 수량 차감 - 차감 수량이 더 커서 실패")
    void subtractFail() {
        //Given
        Option option = new Option("name", 100);

        //When Then
        assertThatThrownBy(() -> option.subtract(101))
                .isInstanceOf(InsufficientOptionQuantityException.class);
    }
}
