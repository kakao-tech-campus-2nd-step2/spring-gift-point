package gift.product.restapi.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotContainingKaKaoValidator implements ConstraintValidator<NotContainingKaKao, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return !value.contains("카카오");
    }
}