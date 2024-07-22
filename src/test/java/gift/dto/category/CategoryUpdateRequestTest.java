package gift.dto.category;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CategoryUpdateRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("유효한 카테고리 수정")
    public void testValidCategoryUpdateRequest() {
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(
            "Category",
            "#000000",
            "imageUrl",
            "description"
        );

        Set<ConstraintViolation<CategoryUpdateRequest>> violations = validator.validate(
            categoryUpdateRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("필수 필드 누락 - 이름")
    public void testUpdateRequestNoName() {
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(
            null,
            "#000000",
            "imageUrl",
            "description"
        );

        Set<ConstraintViolation<CategoryUpdateRequest>> violations = validator.validate(
            categoryUpdateRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("name") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }

    @Test
    @DisplayName("필수 필드 누락 - 색상 코드")
    public void testUpdateRequestNoColor() {
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(
            "Category",
            null,
            "imageUrl",
            "description"
        );

        Set<ConstraintViolation<CategoryUpdateRequest>> violations = validator.validate(
            categoryUpdateRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("color") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }

    @Test
    @DisplayName("필수 필드 누락 - 이미지 URL")
    public void testUpdateRequestNoImageUrl() {
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(
            "Category",
            "#000000",
            null,
            "description"
        );

        Set<ConstraintViolation<CategoryUpdateRequest>> violations = validator.validate(
            categoryUpdateRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("imageUrl") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }
}
