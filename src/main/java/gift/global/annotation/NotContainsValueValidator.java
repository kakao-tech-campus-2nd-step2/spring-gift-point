package gift.global.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * NotContainsValue 어노테이션을 위한 유효성 검사기
 */
public class NotContainsValueValidator implements ConstraintValidator<NotContainsValue, String> {

    private String valueToAvoid;

    /**
     * 유효성 검사기 초기화
     */
    @Override
    public void initialize(NotContainsValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.valueToAvoid = constraintAnnotation.value();
    }

    /**
     * 유효성 검사 로직
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value.contains(valueToAvoid)) {
            context.disableDefaultConstraintViolation(); // 기본 유효성 검사 오류 메시지 비활성화
            context.buildConstraintViolationWithTemplate( // 커스텀 오류 메시지를 생성해 유효성 검사 메시지로 추가
                    context.getDefaultConstraintMessageTemplate())
                .addConstraintViolation(); // 유효성 검사 오류를 컨텍스트에 추가
            return false; // MethodArgumentNotValidException 발생
        }
        return true;
    }
}
