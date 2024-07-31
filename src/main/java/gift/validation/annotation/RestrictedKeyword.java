package gift.validation.annotation;

import gift.validation.validator.RestrictedKeywordValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Constraint(validatedBy = RestrictedKeywordValidator.class)
public @interface RestrictedKeyword {

    String message() default "제한된 문구가 포함되어 있습니다.";

    Class[] groups() default {};

    Class[] payload() default {};

    String[] keywords() default {};
}
