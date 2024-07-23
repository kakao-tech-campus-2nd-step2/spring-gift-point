package gift.dto.common.valid;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/*
 * @deprecated Replaced by ValidProductName
 */
@Deprecated
@Target({FIELD, TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ProductNameSymbolValidator.class)
@Documented
public @interface ValidProductNameSymbol {
    String message() default "( ), [ ], +, -, &, /, _ 외 특수문자는 사용할 수 없습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


