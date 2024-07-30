package gift.entity;

import static org.assertj.core.api.Assertions.assertThat;

import gift.dto.OptionEditRequest;
import gift.dto.OptionSubtractRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OptionTest {

    private final Category category = new Category("생일선물", "Red", "http", "생일선물 카테고리");
    private final Product product = new Product("kakao", 5000L, "https", category);
    private final Option option = new Option("Large", 100, product);

    @Test
    @DisplayName("옵션 정보 수정 테스트")
    void updateOption() {
        OptionEditRequest editRequest = new OptionEditRequest(null, "Medium", 30, null);

        option.updateOption(editRequest);
        assertThat(option.getName()).isEqualTo(editRequest.getName());
        assertThat(option.getQuantity()).isEqualTo(editRequest.getQuantity());
    }

    @Test
    @DisplayName("옵션 개수 차감 테스트")
    void subtractQuantity() {
        option.subtractQuantity(new OptionSubtractRequest(null, 100, null));

        assertThat(option.getQuantity()).isZero();
    }
}