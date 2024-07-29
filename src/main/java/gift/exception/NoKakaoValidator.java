package gift.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoKakaoValidator implements ConstraintValidator<NoKakao, String> {

    @Override
    public void initialize(NoKakao constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null && value.contains("카카오")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("'카카오'가 포함된 문구는 담당 MD와 협의 후 요청바랍니다.")
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
