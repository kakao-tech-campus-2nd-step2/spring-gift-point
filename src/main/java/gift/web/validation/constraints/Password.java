package gift.web.validation.constraints;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gift.web.validation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {

    String message() default "비밀번호는 영문 대소문자, 숫자를 포함하여 8자 이상 15자 이하로 입력해주세요.";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
