package gift.common.annotation;

import gift.common.validator.InvalidWordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InvalidWordValidator.class)
public @interface InvalidWord {
    String message() default "Invalid word";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String value();
}
