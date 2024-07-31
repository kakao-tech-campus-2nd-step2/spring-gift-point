package gift.core.annotaions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import gift.core.exception.ValidationMessage;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = CharSetValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCharset {
	String message() default ValidationMessage.INVALID_CHARSET_MSG;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String pattern() default "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_\\s]*$";
}
