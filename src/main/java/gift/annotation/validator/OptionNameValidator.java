package gift.annotation.validator;

import gift.annotation.OptionName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionNameValidator implements ConstraintValidator<OptionName, String> {


    private static final String regex = "^[a-zA-Z0-9 ()\\[\\]+\\-\\&/_]{1,50}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return value.matches(regex);
    }
}