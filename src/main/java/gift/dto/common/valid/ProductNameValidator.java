package gift.dto.common.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ProductNameValidator implements ConstraintValidator<ValidProductName, String> {

    private static final Pattern SYMBOL_PATTERN = Pattern.compile(
        "^[\\w가-힣ㄱ-ㅎㅏ-ㅣ()\\[\\]+\\-&/_]*$");

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.isBlank()) {
            addConstraintViolation(context, "필수 입력 값입니다");
            return false;
        }

        if (name.length() > 15) {
            addConstraintViolation(context, "최대 15자까지 입력할 수 있습니다.");
            return false;
        }

        if (!SYMBOL_PATTERN.matcher(name).matches()) {
            addConstraintViolation(context, "( ), [ ], +, -, &, /, _ 외 특수문자는 사용할 수 없습니다.");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
            .addConstraintViolation();
    }
}
