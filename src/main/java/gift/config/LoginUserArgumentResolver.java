package gift.config;

import gift.auth.jwt.JwtProvider;
import gift.domain.member.entity.Member;
import gift.domain.member.repository.MemberJpaRepository;
import gift.exception.InvalidAuthException;
import io.jsonwebtoken.Claims;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    public static final int NUMBER_OF_HEADER_PARTS = 2;
    public static final String HEADER_TYPE = "Bearer";

    private final JwtProvider jwtProvider;
    private final MemberJpaRepository memberJpaRepository;

    public LoginUserArgumentResolver(JwtProvider jwtProvider, MemberJpaRepository memberJpaRepository) {
        this.jwtProvider = jwtProvider;
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean isUserType = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasAnnotation && isUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String field = webRequest.getHeader("Authorization");
        if (field == null) {
            throw new InvalidAuthException("error.invalid.token.header");
        }

        String[] splitField = field.split(" ");
        if ((splitField.length != NUMBER_OF_HEADER_PARTS) || (!splitField[0].equals(HEADER_TYPE))) {
            throw new InvalidAuthException("error.invalid.token.header");
        }

        Claims claims = jwtProvider.getAuthentication(splitField[1]);
        return memberJpaRepository.findById(Long.parseLong(claims.getSubject()))
            .orElseThrow(() -> new InvalidAuthException("error.invalid.token"));
    }
}
