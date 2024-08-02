package gift.test;


import gift.exception.InvalidProductNameException;
import gift.validator.ProductNameValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class ProductServiceTest {


    @Test
    @DisplayName("상품 이름이 20글자가 넘어갈때 예외발생")
    void testInvalidNameLength() {
        String longName = "veryveryveryveryveryveryLong";
        InvalidProductNameException exception = Assertions.assertThrows(
            InvalidProductNameException.class, () -> {
                ProductNameValidator.validateProductName(longName);
            });
        Assertions.assertEquals("상품 이름은 공백 포함 20글자를 넘을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("상품 이름에 허용되지 않은 특수기호가 들어갈때 예외발생")
    void testInvalidCharacters() {
        String invalidName = "Invalid@Name#";
        InvalidProductNameException exception = Assertions.assertThrows(InvalidProductNameException.class, () -> {
            ProductNameValidator.validateProductName(invalidName);
        });
        Assertions.assertEquals("상품 이름에는 특수문자 ( ), [ ], +, -, &, /, _ 만 사용할 수 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("상품 이름에 카카오 라는 문구가 들어갈때 예외발생")
    void testContainsKakao() {
        String nameWithKakao = "카카오Product";
        InvalidProductNameException exception = Assertions.assertThrows(InvalidProductNameException.class, () -> {
            ProductNameValidator.validateProductName(nameWithKakao);
        });
        Assertions.assertEquals("상품 이름에 '카카오'가 포함되면 담당 MD와 협의가 필요합니다.", exception.getMessage());
    }
}
