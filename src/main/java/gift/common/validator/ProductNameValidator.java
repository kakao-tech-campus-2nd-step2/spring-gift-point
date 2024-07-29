package gift.common.validator;

import gift.common.annotation.ProductNameValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductNameValidator implements ConstraintValidator <ProductNameValid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.contains("카카오")) {
            return false;
        }

        if (!areBracketsMatched(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("괄호의 짝이 맞지 않습니다.")
                .addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean areBracketsMatched(String value) {
        int roundBrackets = 0;
        int squareBrackets = 0;

        for (char c : value.toCharArray()) {
            switch (c) {
                case '(':
                    roundBrackets++;
                    break;
                case ')':
                    roundBrackets--;
                    break;
                case '[':
                    squareBrackets++;
                    break;
                case ']':
                    squareBrackets--;
                    break;
            }

            if (roundBrackets < 0 || squareBrackets < 0) {
                return false;
            }
        }

        return roundBrackets == 0 && squareBrackets == 0;
    }
}
