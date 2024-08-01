package gift.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidProductNameValidator implements ConstraintValidator<ValidProductName, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.contains("카카오")) {
            addConstraintViolation(context, "카카오가 포함된 문구는 담당 MD와 상의하세요");
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
