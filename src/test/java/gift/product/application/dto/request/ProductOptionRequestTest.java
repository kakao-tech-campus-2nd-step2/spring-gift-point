package gift.product.application.dto.request;

import static gift.common.validation.ValidateErrorMessage.INVALID_OPTION_NAME_LENGTH;
import static gift.common.validation.ValidateErrorMessage.INVALID_OPTION_NAME_NULL;
import static gift.common.validation.ValidateErrorMessage.INVALID_OPTION_QUANTITY_RANGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductOptionRequestTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("ProductOptionRequest 객체 생성 테스트[Name이 비어있는 경우]")
    void createOptionRequestWithNameBlankTest() {
        // given
        String name = "";
        Integer quantity = 1;

        // when
        ProductOptionRequest productOptionRequest = new ProductOptionRequest(name, quantity);
        Set<ConstraintViolation<ProductOptionRequest>> violations = validator.validate(productOptionRequest);

        // then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(INVALID_OPTION_NAME_NULL)
                .isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductOptionRequest 객체 생성 테스트[Name이 null인 경우]")
    void createOptionRequestWithNameNullTest() {
        // given
        String name = null;
        Integer quantity = 1;

        // when
        ProductOptionRequest productOptionRequest = new ProductOptionRequest(name, quantity);
        Set<ConstraintViolation<ProductOptionRequest>> violations = validator.validate(productOptionRequest);

        // then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(INVALID_OPTION_NAME_NULL)
                .isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductOptionRequest 객체 생성 테스트[name길이가 50 초과인 경우]")
    void createOptionRequestWithNameLengthOver50Test() {
        // given
        String name = "1".repeat(51);
        Integer quantity = 1;

        // when
        ProductOptionRequest productOptionRequest = new ProductOptionRequest(name, quantity);
        Set<ConstraintViolation<ProductOptionRequest>> violations = validator.validate(productOptionRequest);

        // then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(INVALID_OPTION_NAME_LENGTH)
                .isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductOptionRequest 객체 생성 테스트[공백을 포함한 이름 길이가 50자 초과인 경우]")
    void createOptionRequestWithNameLengthOver50WithBlankTest() {
        // given
        String name = "1 ".repeat(25).concat("1");
        Integer quantity = 1;

        // when
        ProductOptionRequest productOptionRequest = new ProductOptionRequest(name, quantity);
        Set<ConstraintViolation<ProductOptionRequest>> violations = validator.validate(productOptionRequest);

        // then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(INVALID_OPTION_NAME_LENGTH)
                .isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductOptionRequest 객체 생성 테스트[quantity가 0이하 경우]")
    void createOptionRequestWithQuantityLessThan1Test() {
        // given
        String name = "test";
        Integer quantity = 0;

        // when
        ProductOptionRequest productOptionRequest = new ProductOptionRequest(name, quantity);
        Set<ConstraintViolation<ProductOptionRequest>> violations = validator.validate(productOptionRequest);

        // then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(INVALID_OPTION_QUANTITY_RANGE)
                .isEqualTo(violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("ProductOptionRequest 객체 생성 테스트[quantity가 100,000,000 이상인인 경우]")
    void createOptionRequestWithQuantityOver100000000Test() {
        // given
        String name = "name";
        Integer quantity = 100_000_000;

        // when
        ProductOptionRequest productOptionRequest = new ProductOptionRequest(name, quantity);
        Set<ConstraintViolation<ProductOptionRequest>> violations = validator.validate(productOptionRequest);

        //then
        assertThat(violations.size()).isEqualTo(1);
        assertThat(INVALID_OPTION_QUANTITY_RANGE)
                .isEqualTo(violations.iterator().next().getMessage());
    }
}