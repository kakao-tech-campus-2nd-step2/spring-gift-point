package gift.common.validator;

import gift.common.annotation.InvalidWord;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InvalidWordValidator implements ConstraintValidator<InvalidWord, String> {

    private String invalidWord;
    @Override
    public void initialize(InvalidWord constraintAnnotation) {
        invalidWord = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        return !value.contains(invalidWord);
    }
}
