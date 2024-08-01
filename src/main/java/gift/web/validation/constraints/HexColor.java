package gift.web.validation.constraints;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gift.web.validation.validator.HexColorValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = HexColorValidator.class)
public @interface HexColor {

    String message() default "올바른 색상 코드를 입력해주세요.";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
