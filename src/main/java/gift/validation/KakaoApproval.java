package gift.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = KakaoApprovalValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface KakaoApproval {

    String message() default "상품 이름에 '카카오'를 포함할 수 없습니다. 담당 MD와 협의하세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
