package gift.dto.product;

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

public class ProductCreateRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("유효한 상품 추가")
    public void testAddProductValid() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(
            "Valid Name",
            100,
            "valid.jpg",
            1L
        );

        Set<ConstraintViolation<ProductCreateRequest>> violations = validator.validate(
            productCreateRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("필수 필드 누락 - 이름")
    public void testAddProductNameMissing() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(
            null,
            100,
            "valid.jpg",
            1L
        );

        Set<ConstraintViolation<ProductCreateRequest>> violations = validator.validate(
            productCreateRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("name") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }

    @Test
    @DisplayName("필수 필드 누락 - 가격")
    public void testAddProductPriceMissing() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(
            "Valid Name",
            null,
            "valid.jpg",
            1L
        );

        Set<ConstraintViolation<ProductCreateRequest>> violations = validator.validate(
            productCreateRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("price") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }

    @Test
    @DisplayName("필수 필드 누락 - 이미지 URL")
    public void testAddProductImageUrlMissing() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(
            "Valid Name",
            100,
            null,
            1L
        );

        Set<ConstraintViolation<ProductCreateRequest>> violations = validator.validate(
            productCreateRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("imageUrl") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }

    @Test
    @DisplayName("필수 필드 누락 - 카테고리 ID")
    public void testAddProductCategoryIdMissing() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(
            "Valid Name",
            100,
            "valid.jpg",
            null
        );

        Set<ConstraintViolation<ProductCreateRequest>> violations = validator.validate(
            productCreateRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("categoryId") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }
}
