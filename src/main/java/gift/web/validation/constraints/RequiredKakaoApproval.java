package gift.web.validation.constraints;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gift.web.validation.validator.KakaoApprovalValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = KakaoApprovalValidator.class)
public @interface RequiredKakaoApproval {

    String message() default "담당 MD와 협의한 경우에만 사용가능한 키워드가 존재합니다.";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
