package gift.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gift.product.domain.Product;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ProductTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testProductNameTooLong() {
        Product product = new Product();
        product.setName("최대15글자를 넘어가는 이름123456789");
        product.setPrice(1000);
        product.setImageUrl("http://example.com/image.jpg");

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertEquals(1, violations.size());
        assertEquals("이름은 최대 15글자입니다.", violations.iterator().next().getMessage());
    }

    @Test
    public void testProductNameWithInvalidCharacters() {
        Product product = new Product();
        product.setName("<><>><>_+_+>@#");
        product.setPrice(1000);
        product.setImageUrl("http://example.com/image.jpg");

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertEquals(1, violations.size());
        assertEquals("특수문자는 (),[],+,-,&,/,_만 허용됩니다.",
            violations.iterator().next().getMessage());
    }

    @Test
    public void testProductNameContainsKakao() {
        Product product = new Product();
        product.setName("카카오톡");
        product.setPrice(1000);
        product.setImageUrl("http://example.com/image.jpg");

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertEquals(1, violations.size());
        assertEquals("'카카오'가 들어간 제품명은 MD와 상의해주세요.",
            violations.iterator().next().getMessage());
    }

    @Test
    public void testProductValid() {
        Product product = new Product();
        product.setName("아메리카노");
        product.setPrice(1000);
        product.setImageUrl("http://example.com/image.jpg");

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertEquals(0, violations.size());
    }
}

