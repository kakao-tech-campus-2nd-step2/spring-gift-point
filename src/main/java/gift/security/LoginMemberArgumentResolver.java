package gift.security;

import gift.user.service.UserService;
import gift.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

  private final UserService userService;
  private final JWTUtil jwtUtil;

  @Autowired
  public LoginMemberArgumentResolver(UserService userService, JWTUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(LoginMember.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, org.springframework.web.bind.support.WebDataBinderFactory binderFactory) throws Exception {
    String token = extractToken(webRequest);
    if (token != null) {
      if (jwtUtil.isExpired(token)) {
        throw new AuthenticationFailedException("토큰이 만료되었습니다.");
      }
      String email = jwtUtil.getEmail(token);
      return userService.getUserByEmail(email);
    }
    return null;
  }

  private String extractToken(NativeWebRequest webRequest) {
    String bearerToken = webRequest.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}