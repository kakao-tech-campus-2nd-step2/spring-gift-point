package gift.product.validation;


import gift.product.message.ProductInfo;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidNamePatternValidator.class)
public @interface ValidNamePattern {
    String message() default ProductInfo.PRODUCT_NAME_PATTERN;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
