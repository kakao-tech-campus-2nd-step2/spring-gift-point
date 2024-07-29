package gift.annotation;

import gift.annotation.validator.OptionNameValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OptionNameValidator.class)
public @interface OptionName {

    String message() default "Option Name 규칙"; // 기본 메시지 설정

    Class[] groups() default {};

    Class[] payload() default {};
}
