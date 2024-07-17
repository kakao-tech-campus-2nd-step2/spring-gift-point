package gift.product.model.dto.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExcludeKeywordValidator implements ConstraintValidator<ExcludeKeyword, String> {
    private String excludeKeyword;

    @Override
    public void initialize(ExcludeKeyword constraintAnnotation) {
        this.excludeKeyword = constraintAnnotation.excludeKeyword();
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (!excludeKeyword.isEmpty() && name.contains(excludeKeyword)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            String.format("상품 이름에 '%s'를 포함할 수 없습니다. 담당 MD와 협의 후 사용해주세요.", excludeKeyword))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
