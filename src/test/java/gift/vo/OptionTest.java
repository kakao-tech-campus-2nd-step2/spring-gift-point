package gift.vo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class OptionTest {

    Product mockProduct;

    @BeforeEach
    void setUp() {
        mockProduct = Mockito.mock(Product.class);
    }

    @Test
    @DisplayName("Test for Option Name Exceeding 50 Characters")
    void Exceed50Characters() {
        // given
        String falseName = "옵션명".repeat(50);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Option(null, mockProduct, falseName, 500))
                .withMessage("옵션명은 공백 포함하여 최대 50자까지 가능합니다.");
    }
    
    @Test
    @DisplayName("Test for special characters excluding () [] + - & /")
    void CheckSpecialCharacters() {
        // given
        String falseName = "**특가**";

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy( () -> new Option(null, mockProduct, falseName, 500))
                .withMessage("상품명에 () [] + - & / 외의 특수기호는 불가합니다");
    }
    @Test
    @DisplayName("Test for Success subtract option quantity")
    void successfulQuantitySubtraction() {
        // given
        Option option = new Option(null, mockProduct, "true", 500);

        // when
        option.subtractQuantity(300);

        // then
        assertThat(option.getQuantity()).isEqualTo(200);
    }

    @Test
    @DisplayName("Test for unSuccess subtract option quantity")
    void unsuccessfulQuantitySubtraction() {
        // given
        Option option = new Option(null, mockProduct, "true", 500);

        // When & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> option.subtractQuantity(600))
                .withMessage("해당 상품 옵션의 재고가 선택하신 수량 보다 작습니다. " + "[남은 수량: "+option.getQuantity()+"]" );
    }

    @Test
    @DisplayName("Test for invalid quantity outside 1 <= quantity <100000000 range")
    void invalidQuantityOutsideRange() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Option(null, mockProduct,"true", 0))
                .withMessage("옵션 수량은 최소 1개 이상 1억 개 미만이여야 합니다.");

    }
}