package gift.product.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidNamePatternValidator implements ConstraintValidator<ValidNamePattern, String> {

    private static final String VALID_NAME_REGEX = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣()\\[\\]+\\-&/_ ]+$";

    @Override
    public void initialize(ValidNamePattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isEmpty()) {
            return true; // @NotBlank 어노테이션을 적용하므로 여기서는 null 체크 불필요
        }

        return value.matches(VALID_NAME_REGEX);
    }
}
