package gift.validation;

import gift.product.dto.OptionRequest;
import gift.product.dto.ProductRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class ProductRequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private final OptionRequest option = new OptionRequest("옵션", 10);

    @BeforeEach
    public void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    public void close() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("정상 상품 유효성 검사 테스트")
    void checkNormalProduct() {
        ProductRequest request = new ProductRequest(
                "product",
                1000,
                "https://shop.com",
                "상품권",
                option);

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이름이 15자 초과인 상품 유효성 검사 테스트")
    void checkLongerNameProduct() {
        ProductRequest request = new ProductRequest(
                "productproductproduct",
                1000,
                "https://shop.com",
                "상품권",
                option);

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("이름에 특수 문자가 포함된 상품 유효성 검사 테스트")
    void checkEscapeCharacterProduct() {
        ProductRequest request = new ProductRequest(
                "pr@duct",
                1000,
                "https://shop.com",
                "상품권",
                option);

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("이름에 사용 가능한 특수 문자가 포함된 상품 유효성 검사 테스트")
    void checkCorrectEscapeCharacterProduct() {
        ProductRequest request = new ProductRequest(
                "[(+-_&/)]",
                1000,
                "https://shop.com",
                "상품권",
                option);

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이름에 '카카오'가 포함된 상품 유효성 검사 테스트")
    void checkKakaoProduct() {
        ProductRequest request = new ProductRequest(
                "카카오 product",
                1000,
                "https://shop.com",
                "상품권",
                option);

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("가격이 음수인 상품 유효성 검사 테스트")
    void checkMinusPriceProduct() {
        ProductRequest request = new ProductRequest(
                "product",
                -1000,
                "https://shop.com",
                "상품권",
                option);

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

}
