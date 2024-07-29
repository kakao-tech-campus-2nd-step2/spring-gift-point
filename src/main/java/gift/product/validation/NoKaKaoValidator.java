package gift.product.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoKaKaoValidator implements ConstraintValidator<NoKaKao, String> {

    @Override
    public void initialize(NoKaKao constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotNull 애노테이션으로 null을 검증할 수 있으므로 여기서는 true 반환
        }
        return !value.contains("카카오");
    }
}
