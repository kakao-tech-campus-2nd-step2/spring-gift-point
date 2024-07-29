package gift.global.auth;

import gift.model.member.Role;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 인가를 위한 어노테이션 아래 어노테이션이 붙으면 Admin을 제외하고 해당 role이 아닌 사용자는 접근할 수 없음
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authorization {

    Role role() default Role.USER;

}
