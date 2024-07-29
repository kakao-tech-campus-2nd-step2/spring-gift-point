package gift.product.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class KakaoNamePatternValidator implements ConstraintValidator<KakaoNamePattern, String> {

    private static final String KAKAO_NAME = "카카오";

    @Override
    public void initialize(KakaoNamePattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isEmpty()) {
            return true; // @NotBlank 어노테이션을 적용하므로 여기서는 null 체크 불필요
        }

        return !value.contains(KAKAO_NAME); // 카카오 이름이 포함되어 있으면 false
    }
}
