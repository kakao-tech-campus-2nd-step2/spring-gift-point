package gift.main.interceptor;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.dto.UserVo;
import gift.main.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final String BEARER = "Bearer ";
    private final JwtUtil jwtUtil;

    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith(BEARER)) {
            throw new CustomException(ErrorCode.NO_TOKEN);
        }
        String token = authorization.split(" ")[1];


        if (token == null) {
//            response.sendRedirect("/spring-gift/members/login");
            throw new CustomException(ErrorCode.NO_TOKEN);

        }

        if (!jwtUtil.validateToken(token)) {
//            response.sendRedirect("/spring-gift/members/login");
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        UserVo sessionUser = new UserVo(
                jwtUtil.getId(token),
                jwtUtil.getName(token),
                jwtUtil.getEmail(token),
                jwtUtil.getRole(token));

        HttpSession session = request.getSession(true);

        session.setAttribute("user", sessionUser);

        return true;
    }

    @Override
    //컨트롤러 실행후 (예외 발생안해여!)
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    // 뷰 실행 후 (컨트롤러에서 발생 예외 여기로 전송된다링구 (링구먼지암?ㅋ 링커스 친구들임)
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HttpSession session = request.getSession(false);
        session.invalidate();
    }
}
