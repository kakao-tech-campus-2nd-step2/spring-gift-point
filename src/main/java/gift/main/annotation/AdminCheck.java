package gift.main.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE}) //적용될 부분
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminCheck {
}
