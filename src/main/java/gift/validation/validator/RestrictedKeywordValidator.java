package gift.validation.validator;

import gift.validation.annotation.RestrictedKeyword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class RestrictedKeywordValidator implements ConstraintValidator<RestrictedKeyword, String> {

    private String[] keywords;

    @Override
    public void initialize(RestrictedKeyword constraintAnnotation) {
        this.keywords = constraintAnnotation.keywords();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        return Arrays.stream(keywords).noneMatch(value::contains);
    }
}
