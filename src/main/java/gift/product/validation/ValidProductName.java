package gift.product.validation;

import gift.product.message.ProductInfo;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotBlank(message = ProductInfo.PRODUCT_NAME_REQUIRED)
@Size(min = 1, max = 15, message = ProductInfo.PRODUCT_NAME_SIZE)
@KakaoNamePattern
@ValidNamePattern
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface ValidProductName {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
