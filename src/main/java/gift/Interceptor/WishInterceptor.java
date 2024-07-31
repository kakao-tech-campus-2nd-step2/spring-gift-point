package gift.Interceptor;

import gift.Exception.AuthorizedException;
import gift.Model.Entity.MemberEntity;
import gift.Repository.MemberRepository;
import gift.Token.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Component
public class WishInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public WishInterceptor(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository){
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Bearer");
        String email = jwtTokenProvider.getEmailFromToken(token);

        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);

        if(memberOptional.isEmpty()){
            throw new AuthorizedException();
        }

        request.setAttribute("Email", email);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 컨트롤러 실행 후에 수행될 로직을 구현합니다.

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
        // 뷰가 렌더링된 후에 수행될 로직을 구현합니다.

    }
}
