package gift.global.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)// 메서드 파라미터에 @Authenticate 적용가능
@Retention(RetentionPolicy.RUNTIME)// 라이프사이클 -> 런타임 동안 읽을 수 있음
public @interface LoginUser {

}
