package gift.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final ApplicationContext applicationContext;
    private final TokenExtractor tokenExtractor;

    @Autowired
    public TokenInterceptor(ApplicationContext applicationContext, TokenExtractor tokenExtractor) {
        this.applicationContext = applicationContext;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = tokenExtractor.getToken();

        //OrderController에서 @LoginMemberResolver로 토큰에 대한 유효성은 이미 검증
        //검증된 토큰을 가져오는 것이므로 유효성 검증은 불필요하다.
        if(token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false; // 요청을 중단하고 401 응답을 반환하도록 함.
        }

        request.setAttribute("token", token);
        return true; //토큰이 유효함을 확인한 이후기 때문에 계속함
    }
}
