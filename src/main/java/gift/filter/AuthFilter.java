package gift.filter;

import gift.domain.AuthToken;
import gift.repository.token.TokenRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static gift.utils.FilterConstant.*;

public class AuthFilter implements Filter {

    private final TokenRepository tokenRepository;

    public AuthFilter(TokenRepository tokenRepository) {
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

        // Filter 를 통과하지 않아도 되는 url
        if (path.equals(HOME_URL) || path.equals(KAKAO_TOKEN_RENEW_URL) || path.startsWith(LOGIN_URL_PREFIX) || path.startsWith(LOGIN_OAUTH_URL_PREFIX) || path.startsWith(H2_DB_URL)
                || path.equals("/swagger-ui.html") // 변경
                || path.startsWith("/swagger-ui")
                || path.startsWith("/api-docs") // 추가
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization header 존재하는지 확인
        // 없으면 누구나 접근할 수 있는 페이지로 리다이렉트
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || authHeader.isEmpty()){
            httpResponse.sendRedirect(NO_AUTHORIZATION_REDIRECT_URL);
            return;
        }

        Optional<AuthToken> token = tokenRepository.findAuthTokenByToken(authHeader.substring(7));

        if (token.isEmpty()){
            httpResponse.sendRedirect(NO_AUTHORIZATION_REDIRECT_URL);
            return;
        }

        httpRequest.setAttribute("AuthToken",token.get());
        filterChain.doFilter(request, response);
    }


}
