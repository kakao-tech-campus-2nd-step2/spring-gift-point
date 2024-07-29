package gift.model.gift;

import gift.model.category.Category;
import gift.model.option.Option;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GiftTest {

    @Test
    void checkThrowExceptionContainsKakao() {
        String invalidName = "카카오 문구";
        int price = 1000;
        String imageUrl = "test.jpg";

        Category category = new Category(10L, "test", "test", "test", "test");
        Option option1 = new Option(10L, "testOption", 1);
        List<Option> option = Arrays.asList(option1);

        assertThatThrownBy(() -> new Gift(invalidName, price, imageUrl, category, option))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("카카오 문구는 MD와 협의 후 사용가능합니다.");
    }

}