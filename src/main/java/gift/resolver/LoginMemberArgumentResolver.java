package gift.resolver;

import gift.annotation.LoginMember;
import gift.model.Member;
import gift.service.MemberService;
import gift.util.JwtUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

  private final JwtUtil jwtUtil;
  private final MemberService memberService;

  public LoginMemberArgumentResolver(JwtUtil jwtUtil, MemberService memberService) {
    this.jwtUtil = jwtUtil;
    this.memberService = memberService;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(LoginMember.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, org.springframework.web.bind.support.WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    String token = request.getHeader("Authorization").substring(7);
    Long memberId = jwtUtil.getMemberIdFromToken(token);
    return memberService.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid token"));
  }
}
