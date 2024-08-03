package gift.Service;

import gift.Exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.util.StringUtils;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    private final AuthorizationExtractor authExtractor;
    private final MemberAccessTokenProvider memberAccessTokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor, MemberAccessTokenProvider memberAccessTokenProvider) {
        this.authExtractor = authExtractor;
        this.memberAccessTokenProvider = memberAccessTokenProvider;
    }

    @Override //
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = authExtractor.extract(request);//토큰 추출
        if (StringUtils.isEmpty(token)) {// 토큰이 비어있는지 체크해 비어있으면 토큰을 비어있으면 false
            throw new UnauthorizedException("401 Unauthorized : Invalid or missing token");
        }
        String email = memberAccessTokenProvider.getEmail(token);//토큰을 디코딩해 email을 얻음
        request.setAttribute("email", email);// request에 email값세팅
        return true;
    }
}
