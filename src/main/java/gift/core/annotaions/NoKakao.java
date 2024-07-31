package gift.core.annotaions;



import gift.core.exception.ValidationMessage;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoKakaoValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoKakao {
	String message() default ValidationMessage.NO_KAKAO_MSG;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}



