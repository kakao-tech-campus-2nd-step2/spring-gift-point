package gift.annotation.validator;

import gift.annotation.ProductName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductNameValidator implements ConstraintValidator<ProductName, String> {

    private static final String regex = "^[a-zA-Z0-9 \\(\\)\\[\\]\\+\\-\\&\\/\\_]{1,15}$"; // 허용되는 문자의 정규식.

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if (value.contains("카카오")) {
            return false;
        }

        return value.matches(regex);
    }
}
