package gift.filter;


import gift.domain.AuthToken;
import gift.repository.token.TokenRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

import static gift.utils.FilterConstant.*;

/**
 * post /login/token 요청 시 Authorization 토큰 값을 이미 가지고 있다면 /home(누구나 접근할 수 있는 페이지) 으로 리다이렉션 하기 위한 필터
 */
public class MyTokenFilter implements Filter {

    private final TokenRepository tokenRepository;

    public MyTokenFilter(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        if (path.equals(HOME_URL) || path.equals(KAKAO_TOKEN_RENEW_URL) || path.startsWith(LOGIN_URL_PREFIX) || path.startsWith(LOGIN_OAUTH_URL_PREFIX) || path.startsWith(H2_DB_URL)
                || path.equals(SWAGGER_UI_HTML) // 변경
                || path.startsWith(SWAGGER_UI)
                || path.startsWith(API_DOCS) // 추가
                || path.startsWith(V3_API_DOCS)
                || path.startsWith(SWAGGER_RESOURCES)) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthToken authToken = (AuthToken) httpRequest.getAttribute("AuthToken");

        if(isMyServerToken(authToken)){
            if(isMyServerTokenExpired(authToken)){
                tokenRepository.deleteById(authToken.getId());
                httpResponse.sendRedirect(NO_AUTHORIZATION_REDIRECT_URL);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isMyServerTokenExpired(AuthToken authToken) {
        return authToken.getCreatedDate().plusSeconds(authToken.getTokenTime()).isBefore(LocalDateTime.now());
    }

    private boolean isMyServerToken(AuthToken authToken) {
        return authToken.getAccessToken() == null;
    }

}
