package gift.validation.validator;

import gift.validation.constraint.EmailConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserEmailValidator implements ConstraintValidator<EmailConstraint, String> {

    @Override
    public void initialize(EmailConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String emailField, ConstraintValidatorContext cxt) {

        String emailFormatErrorMsg = "올바른 이메일을 작성하십쇼";

        boolean emailFormatValid = emailField.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
        if (!emailFormatValid) {
            cxt.disableDefaultConstraintViolation();
            cxt.buildConstraintViolationWithTemplate(emailFormatErrorMsg)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
