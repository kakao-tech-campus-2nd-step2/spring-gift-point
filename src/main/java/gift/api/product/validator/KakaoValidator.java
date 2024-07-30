package gift.api.product.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class KakaoValidator implements ConstraintValidator<NoKakao, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !value.contains("카카오");
    }
}
