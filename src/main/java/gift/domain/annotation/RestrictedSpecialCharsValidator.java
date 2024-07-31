package gift.domain.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class RestrictedSpecialCharsValidator implements ConstraintValidator<RestrictedSpecialChars, CharSequence> {

    private Pattern pattern;

    public String quote(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            result.append("\\");
            result.append(input.charAt(i));
        }
        return result.toString();
    }

    @Override
    public void initialize(RestrictedSpecialChars constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        String regex = "^[\\w\\s" + quote(constraintAnnotation.value()) + "가-힣ㄱ-ㅎㅏ-ㅣ]*$";
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext) {
        if (charSequence == null) {
            return true;
        }
        return pattern.matcher(charSequence).matches();
    }
}
