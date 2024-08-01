package gift;

import gift.DTO.MemberDto;
import gift.Exception.UnauthorizedException;
import gift.Service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

  public static final String TOKEN_TYPE = "Bearer ";
  private final JwtService jwtService;

  public LoginUserArgumentResolver(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(LoginUser.class) != null;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_TYPE)) {
      throw new UnauthorizedException("No Bearer token found in request headers");
    }
    String token = authorizationHeader.substring(TOKEN_TYPE.length());
    MemberDto memberDto = jwtService.getUserEmailFromToken(token);
    return memberDto;
  }
}
