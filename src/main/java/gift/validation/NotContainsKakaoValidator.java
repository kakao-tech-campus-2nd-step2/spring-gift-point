package gift.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotContainsKakaoValidator implements ConstraintValidator<NotContainsKakao, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !value.contains("카카오");
    }

}
