package gift.validation;

import gift.product.dto.CategoryRequest;
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

public class CategoryRequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

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
    @DisplayName("정상 카테고리 유효성 검사 테스트")
    void checkNormalCategory() {
        CategoryRequest request = new CategoryRequest(
                "상품권",
                "#ffffff",
                "https://product-shop.com",
                ""
        );

        Set<ConstraintViolation<CategoryRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이름이 없는 카테고리 유효성 검사 테스트")
    void checkEmptyNameCategory() {
        CategoryRequest request = new CategoryRequest(
                "",
                "#ffffff",
                "https://product-shop.com",
                ""
        );

        Set<ConstraintViolation<CategoryRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("유효하지 않은 색상 코드인 카테고리 유효성 검사 테스트")
    void checkInvalidColorCategory() {
        CategoryRequest request = new CategoryRequest(
                "상품권",
                "123123123",
                "https://product-shop.com",
                ""
        );

        Set<ConstraintViolation<CategoryRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

}
