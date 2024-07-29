package gift.exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NoKakaoValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoKakao {

    String message() default "'카카오'가 포함된 문구는 담당 MD와 협의 후 요청바랍니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
