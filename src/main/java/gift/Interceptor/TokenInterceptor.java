package gift.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TokenExtractor tokenExtractor = applicationContext.getBean(TokenExtractor.class);
        String token = tokenExtractor.getToken();

        if(token == null | !isValidToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false; // 요청을 중단하고 401 응답을 반환하도록 함.
        }

        //token이 카카오 로그인이면 accessToken, 일반 로그인이면 token임. 그러면 로그인 종류가 늘어날 때마다 if문을 추가해야 하는데...
        //쿠키의 이름을 통일해서 사용한다면 토큰 추출 시 KakaoToken인지 어떻게 판단할 수 있을지? 이름이 다르면 또 코드가 길어짐...
        request.setAttribute("accessToken", token);
        return true; //토큰이 유효함을 확인한 이후기 때문에 계속함
    }

    private boolean isValidToken(String token) {
        // 토큰 유효성 검사 로직 추가 필요!
        return true; //임시 true
    }
}
