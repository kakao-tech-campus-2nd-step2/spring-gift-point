package gift.product.validator;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = KakaoConstraintValidator.class)
public @interface KakaoValidator {

    String message() default "현재 '카카오'가 포함된 문구를 입력할 수 없습니다.\n담당 MD와 연락하시길 바랍니다.";

    Class[] groups() default {};

    Class[] payload() default {};

}
