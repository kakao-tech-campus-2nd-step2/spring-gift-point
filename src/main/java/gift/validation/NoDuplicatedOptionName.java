package gift.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NoDuplicatedOptionNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDuplicatedOptionName {

    String message() default "중복된 옵션명입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
