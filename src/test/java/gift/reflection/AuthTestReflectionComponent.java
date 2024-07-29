package gift.reflection;

import gift.controller.auth.AuthInterceptor;
import gift.model.MemberRole;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class AuthTestReflectionComponent {

    private AuthInterceptor authInterceptor;

    public AuthTestReflectionComponent(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    public Long getMemberIdWithToken(String token) {
        try {
            var method = authInterceptor.getClass().getDeclaredMethod("getClaimsWithToken", String.class);
            method.setAccessible(true);
            var claims = (Claims) method.invoke(authInterceptor, token);
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException("토큰으로 ID 복호화하는 과정에서 예외 발생: " + e.getMessage(), e);
        }
    }

    public MemberRole getMemberRoleWithToken(String token) {
        try {
            var method = authInterceptor.getClass().getDeclaredMethod("getClaimsWithToken", String.class);
            method.setAccessible(true);
            var claims = (Claims) method.invoke(authInterceptor, token);
            return MemberRole.valueOf((String) claims.get("role"));
        } catch (Exception e) {
            throw new RuntimeException("토큰으로 ROLE 복호화하는 과정에서 예외 발생: " + e.getMessage(), e);
        }
    }
}
