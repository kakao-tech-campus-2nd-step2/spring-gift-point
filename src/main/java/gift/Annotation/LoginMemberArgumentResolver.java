package gift.Annotation;

import gift.Model.MemberDto;
import gift.Service.MemberService;
import gift.Utils.JwtUtil;
import gift.Validation.KakaoTokenValidator;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private boolean isKakaoToken = false;

    @Autowired
    public LoginMemberArgumentResolver(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(MemberDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();

        // 쿠키에서 JWT 토큰 추출
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }

                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    isKakaoToken = true;
                    break;
                }
            }
        }

        if (token == null) {
            throw new IllegalArgumentException("JWT token not found in cookies");
        }

        if (isKakaoToken) {
            //카카오 토큰 검증
            long kakaoId = KakaoTokenValidator.validateToken(token);
            Optional<MemberDto> memberDto = memberService.findByKakaoId(kakaoId);

            return memberDto.orElseThrow(() -> new IllegalArgumentException("User not found"));
        }

        Claims claims = jwtUtil.decodeToken(token); //decode
        String memberEmail = claims.getSubject(); // subject를 email로 설정했기 때문에 userEmail로 사용
        Optional<MemberDto> memberDto = memberService.findByEmail(memberEmail); //null이라면 인증된 것이 아닐 것이고 null이 아니라면 인증된 것
        if (memberDto.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        return memberDto;

    }

}
