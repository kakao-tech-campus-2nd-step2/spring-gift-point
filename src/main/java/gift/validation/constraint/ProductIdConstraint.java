package gift.validation.constraint;

import gift.validation.validator.ProductIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProductIdValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProductIdConstraint {
    String message() default "Invalid product";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
