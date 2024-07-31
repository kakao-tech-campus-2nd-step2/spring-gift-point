package gift.domain.annotation;

import gift.domain.entity.Member;
import gift.domain.exception.forbidden.MemberNotAdminException;
import gift.domain.service.member.MemberService;
import gift.global.WebConfig.Constants.Domain.Member.Permission;
import gift.global.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class ValidAdminMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ValidAdminMemberArgumentResolver(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ValidAdminMember.class) &&
            Member.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String authorizationHeader = webRequest.getHeader("Authorization");
        log.info("Header/Authorization: \"{}\"", authorizationHeader);

        String userEmail = jwtUtil.getSubject(authorizationHeader);
        Member member = memberService.findByEmail(userEmail);
        if (!member.getPermission().equals(Permission.ADMIN)) {
            throw new MemberNotAdminException();
        }
        return member;
    }
}
