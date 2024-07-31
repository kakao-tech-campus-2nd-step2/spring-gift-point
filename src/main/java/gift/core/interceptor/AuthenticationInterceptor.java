package gift.core.interceptor;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import gift.user.domain.Role;
import gift.core.jwt.JwtProvider;
import gift.dto.request.UserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(1)
public class AuthenticationInterceptor implements HandlerInterceptor {
	private final JwtProvider jwtProvider;

	/*
	 * 인증을 처리한다. 인증이 성공하면 true를 반환함과 동시에, 유저의 정보를 attribute에 담는다.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String rawToken = request.getHeader("Authorization");

		Long userId = null;
		Set<Role> roles = Set.of(Role.GUEST);

		if (rawToken != null) {
			userId = Long.parseLong(jwtProvider.getClaims(rawToken).getSubject());
			String strRoles = jwtProvider.getClaims(rawToken).get("roles", String.class);
			roles = Set.of(strRoles.split(",")).stream().map(Role::valueOf).collect(Collectors.toSet());
		}

		request.setAttribute("userDetails", UserDetails.of(userId, roles));
		return true;
	}
}