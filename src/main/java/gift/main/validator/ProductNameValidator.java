package gift.main.validator;

import gift.main.annotation.IsValidName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductNameValidator implements ConstraintValidator<IsValidName, String> {

    @Override
    public void initialize(IsValidName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("상품 이름은 공백이여서는 안됩니다.")
                    .addConstraintViolation();
            return false;
        }

        if (name.length() > 15) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("상품 이름은 15자 이하로 입력해주세요.")
                    .addConstraintViolation();
            return false;
        }

        if (!name.matches("^[가-힣a-zA-Z0-9 ()\\[\\]\\+\\-&/_]*$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("상품명에 사용할 수 있는 특수 기호는 ( ), [ ], +, -, &, /, _  입니다.")
                    .addConstraintViolation();
            return false;
        }

        if (name.contains("카카오")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("상품명에 \"카카오\"를 입력하려면 관리자에게 문의하세요")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
