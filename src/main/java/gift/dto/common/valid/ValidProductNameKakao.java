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
@Constraint(validatedBy = ProductNameKakaoValidator.class)
@Documented
public @interface ValidProductNameKakao {
    String message() default "\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


