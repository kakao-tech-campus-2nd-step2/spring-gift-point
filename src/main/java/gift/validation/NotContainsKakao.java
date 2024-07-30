package gift.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotContainsKakaoValidator.class)
public @interface NotContainsKakao {

    String message() default "{gift.validation.NotContainsKakao.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
