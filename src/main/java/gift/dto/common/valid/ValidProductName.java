package gift.dto.common.valid;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD, TYPE})
@Retention(RUNTIME)
@NotBlank
@Size(max = 15, message = "최대 15자까지 입력할 수 있습니다.")
//@ValidProductNameKakao
//@ValidProductNameSymbol
//@Constraint(validatedBy = {})
@Constraint(validatedBy = {ProductNameValidator.class})
@Documented
public @interface ValidProductName {
    String message() default "Invalid Product";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
