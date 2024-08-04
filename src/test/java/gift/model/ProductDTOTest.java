package gift.model;

import gift.model.product.ProductDTO;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductDTOTest {

    private ProductDTO product;

    @Autowired
    private Validator validator;

    @BeforeEach
    void setUp() {
        product = new ProductDTO("test", 123, "test.com", 1L);
    }

    @Test
    void 상품_이름은_공백을_포함하여_최대_15자까지만_가능_실패_테스트() {
        // given
        product.setName("a".repeat(16));

        // when
        var validate = validator.validate(product);

        // then
        assertThat(validate).isNotEmpty();
    }

    @Test
    void 상품_이름은_공백을_포함하여_최대_15자까지만_가능_성공_테스트() {
        // given
        product.setName("a".repeat(15));

        // when
        var validate = validator.validate(product);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void 특정_특수문자만_사용가능_실패_테스트() {
        // given
        product.setName("w̴̲̻͍̩̘̪͂̏̿́̂̄̉͘͜͞͝ŕ̛̗͉͓̥̰̼̭̹̆̔͋̑̏ơ̝̲̭͚͓̤͚͔̤̍͛̐̎̅̕͘̕ǹ̴̫͉̭̜̞͙̰̾͛͌͝g̷̠͙̝̬͙͍̞̩̜̘͊͐̌̽̐̇̚̕N̴̨̪͈̼̫̪̄̏̇̈͑̂͘a̡̱̭̠̙̾͛͗́̚͡m̷̡̘͓͉͇̘̦̮͈͆̽͂̎̍̐̉͜e͎̼͚͖͑͂̋́̂̀͟");

        // when
        var validate = validator.validate(product);

        // then
        assertThat(validate).isNotEmpty();
    }

    @Test
    void 특정_특수문자만_사용가능_성공_테스트() {
        // given
        product.setName("()[]+-&/_");

        // when
        var validate = validator.validate(product);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void 이름에_카카오_포함_불가능_실패_테스트() {
        // given
        product.setName("개꿀맛 카카오닙스");

        // when
        var validate = validator.validate(product);

        // then
        assertThat(validate).isNotEmpty();
    }

    @Test
    void 이름에_카카오_포함_불가능_성공_테스트() {
        // given
        product.setName("개꿀맛 각가오닙스");

        // when
        var validate = validator.validate(product);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void 성공_테스트() {
        // given
        // when
        var validate = validator.validate(product);

        // then
        assertThat(validate).isEmpty();
    }
}
