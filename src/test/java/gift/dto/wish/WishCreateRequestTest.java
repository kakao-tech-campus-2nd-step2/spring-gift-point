package gift.dto.wish;

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

public class WishCreateRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("WishCreateRequestDTO 생성 테스트")
    public void testCreateWishCreateRequestDTO() {
        Long productId = 1L;
        WishCreateRequest wishCreateRequest = new WishCreateRequest(productId);

        assertThat(wishCreateRequest.productId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("WishCreateRequestDTO 필드 값 테스트")
    public void testWishCreateRequestDTOFields() {
        Long productId = 2L;
        WishCreateRequest wishCreateRequest = new WishCreateRequest(productId);

        assertThat(wishCreateRequest.productId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("필수 필드 누락 - productId")
    public void testWishCreateRequestProductIdMissing() {
        WishCreateRequest wishCreateRequest = new WishCreateRequest(null);

        Set<ConstraintViolation<WishCreateRequest>> violations = validator.validate(
            wishCreateRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("productId") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }
}
