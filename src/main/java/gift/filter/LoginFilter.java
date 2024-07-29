package gift.filter;


import gift.domain.AuthToken;
import gift.repository.token.TokenRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * post /login/token 요청 시 Authorization 토큰 값을 이미 가지고 있다면 /home(누구나 접근할 수 있는 페이지) 으로 리다이렉션 하기 위한 필터
 */
public class LoginFilter implements Filter {

    private final TokenRepository tokenRepository;

    public LoginFilter(TokenRepository tokenRepository) {
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

        // Authorization header 존재하는지 확인
        // 토큰 발행 되었는데 다시 토큰 발행 요청 시 누구나 접근할 수 있는 페이지로 리다이렉션
        String authHeader = httpRequest.getHeader("Authorization");

        if (!(authHeader == null || authHeader.isEmpty())){
            Optional<AuthToken> findAuthToken = tokenRepository.findAuthTokenByToken(authHeader.substring(7));

            if (findAuthToken.isEmpty()){
                filterChain.doFilter(request, response);
                return;
            }

            httpResponse.sendRedirect("/home");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
