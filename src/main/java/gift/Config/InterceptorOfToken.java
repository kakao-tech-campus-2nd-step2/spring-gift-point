package gift.Config;

import gift.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class InterceptorOfToken implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            sendErrorResponse(response, "인증 헤더가 비었습니다.(토큰이 없습니다.)");
            return false;
        }

        try {
            if (userService.validateToken(token)) {
                return true; // 요청을 계속 진행하여 사용자가 요청한 컨트롤러로 넘김.
            } else {
                sendErrorResponse(response, "유효하지 않은 토큰입니다.");
                return false; // 요청에 false 응답을 줘서 사용자로 하여금 리프레쉬 토큰 요청이 오게 만듦. -> RefreshTokenController로 처리
            }
        } catch (Exception e) {
            sendErrorResponse(response, "토큰 유효성 검증 실패: " + e.getMessage());
            return false;
        }
    }

    // 사용자에게 편리하게 json으로 예외 응답 처리. (추후에 Exception으로 따로 관리할 예정)
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
