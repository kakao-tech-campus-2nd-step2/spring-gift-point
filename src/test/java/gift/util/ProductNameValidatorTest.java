package gift.util;

import gift.dto.product.ProductRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ProductNameValidatorTest {

    @Autowired
    private Validator validator;

    @Test
    public void save_nameSuccess() {
        //given
        ProductRequestDto product = new ProductRequestDto("test", 123, "www.test.com", -1L);

        //when
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(product);

        //then
        assertThat(violations).isEmpty();
    }

    @Test
    public void save_nameLongerThan15Char_eng() {
        //given
        String wrongName = "a".repeat(16);
        ProductRequestDto product = new ProductRequestDto(wrongName, 123, "www.test.com", -1L);

        //when
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(product);

        //then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void save_nameLongerThan15Char_kor() {
        //given
        String wrongName = "가나다라마바사아자차카타파하호우";
        ProductRequestDto product = new ProductRequestDto(wrongName, 123, "www.test.com", -1L);

        //when
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(product);

        //then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void save_nameWithStrangeChar() {
        //given
        String wrongName = "W̡̧̤̜͉̝̼̌̎͛̎̽͒̆̀̌R̢̪̟̯̣̄͑̾͘͡O͇̭̬̘̦̦̭̮͌͐̍́̈́͢͝͝N̞͕̝͚̹̹̓̑͊̊̅͐̎̈́͘͜G̶̛̬͖̙̭̩̾͗͐̽͋̑͜͢_̴̢͖̫̙̗̺̘̫̱̐̓̓͑̾N̠͙̙͓̬̣̾͒̆̋̋͜͝Ȧ̜̙̩̦̘̯͕̰̜͛͆̎́̚͘͝M̸̡̛̲̻̙͔͎̳̄̈̈́͊̉̌̄̕͞ͅȆ̵̤̱̖̻̳̹̰͚̣̍̾͆̈̕̕͝͡͝";
        ProductRequestDto product = new ProductRequestDto(wrongName, 123, "www.test.com", -1L);

        //when
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(product);

        //then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void save_nameWithKakao() {
        //given
        String wrongName = "아프리카 직수입 카카오";
        ProductRequestDto product = new ProductRequestDto(wrongName, 123, "www.test.com", -1L);

        //when
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(product);

        //then
        assertThat(violations).isNotEmpty();
    }
}
