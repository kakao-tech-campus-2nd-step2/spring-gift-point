package gift.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ContainsOnlyAllowedSpecialCharacterValidator.class)
public @interface ContainsOnlyAllowedSpecialCharacter {

    String message() default "{gift.validation.ContainsOnlyAllowedSpecialCharacter.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
