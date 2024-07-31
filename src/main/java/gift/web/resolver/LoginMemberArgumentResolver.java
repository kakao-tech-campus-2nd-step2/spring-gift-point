package gift.web.resolver;

import gift.authentication.annotation.LoginMember;
import gift.authentication.token.JwtResolver;
import gift.authentication.token.Token;
import gift.service.MemberDetailsService;
import gift.web.dto.MemberDetails;
import gift.web.validation.exception.client.InvalidCredentialsException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtResolver jwtResolver;
    private final MemberDetailsService memberDetailsService;
    private final String AUTHORIZATION_HEADER = "Authorization";


    public LoginMemberArgumentResolver(JwtResolver jwtResolver, MemberDetailsService memberDetailsService) {
        this.jwtResolver = jwtResolver;
        this.memberDetailsService = memberDetailsService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(LoginMember.class);
        boolean isAssignable = MemberDetails.class.isAssignableFrom(parameter.getParameterType());
        return hasParameterAnnotation && isAssignable;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String authorization = webRequest.getHeader(AUTHORIZATION_HEADER);
        Token token = Token.from(extractToken(authorization));

        Long memberId = jwtResolver.resolveId(token)
            .orElseThrow(InvalidCredentialsException::new);

        return memberDetailsService.loadUserById(memberId);
    }

    private String extractToken(String Authorization) {
        return Authorization.substring(7);
    }

}
