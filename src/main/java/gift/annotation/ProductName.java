package gift.annotation;

import gift.annotation.validator.ProductNameValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) //변수 위에 사용하는 어노테이션이기 때문에 TARGET = FIELD
@Retention(RetentionPolicy.RUNTIME) // 어노테이션 유지범위 - Runtime
@Constraint(validatedBy = ProductNameValidator.class) // 검증 클래스 설정
public @interface ProductName {

    String message() default "이름 규칙"; // 기본 메시지 설정

    Class[] groups() default {};

    Class[] payload() default {};

}
