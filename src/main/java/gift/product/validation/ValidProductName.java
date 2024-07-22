package gift.product.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ProductNameKeywordValidator.class) // 검증 로직을 수행할 클래스 지정
// Record 라 선언된 변수 name 이 PARAMETER 가 아니라 FIELD 임에 주의해야 한다
@Target({ElementType.FIELD}) // 어노테이션 적용할 위치 지정
@Retention(RetentionPolicy.RUNTIME) // Retention: 유지  어노테이션 유지 정책 지정
public @interface ValidProductName {
    String message() default "Invalid product name"; // 기본 오류 메시지

    Class<?>[] groups() default {}; // 검증 그룹

    Class<? extends Payload>[] payload() default {}; // 페이로드 (메타데이터)
}
