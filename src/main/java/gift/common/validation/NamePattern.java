package gift.common.validation;

import static gift.common.validation.ValidateErrorMessage.INVALID_NAME_PATTERN;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
@Pattern(
        regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣 ()\\[\\]+\\-&/_]*$",
        message = INVALID_NAME_PATTERN
)
@Constraint(validatedBy = {})
public @interface NamePattern {
    String message() default INVALID_NAME_PATTERN;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}