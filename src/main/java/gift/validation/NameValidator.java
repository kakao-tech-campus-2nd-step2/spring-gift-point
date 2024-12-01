package gift.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    private static final String SPECIAL_SYMBOL_ERROR_MESSAGE = "() [] + - & / 외의 특수기호는 불가합니다";
    private static final String KAKAO_ERROR_MESSAGE = "`카카오`가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능합니다";

    @Override
    public void initialize(ValidName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        String errorMessage = isValidName(name);
        if (errorMessage != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
            return false;
        }
        return true;
    }

    public static String isValidName(String name) {
        // Check for 특수기호
        if (!name.matches("^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/]*$")) {
            return SPECIAL_SYMBOL_ERROR_MESSAGE;
        }
        return null;
    }

    public static String isValidKakaoName(String name) {
        // Check for "카카오"
        if (name.contains("카카오")) {
            return KAKAO_ERROR_MESSAGE;
        }
        return null;
    }
}
