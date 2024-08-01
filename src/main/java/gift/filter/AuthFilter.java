package gift.filter;

import gift.domain.AuthToken;
import gift.repository.token.TokenRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static gift.filter.FilterUtils.*;

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

        setCorsHeader(httpResponse);
        if(checkOptionMethod(httpRequest, httpResponse)) return;

        String path = httpRequest.getRequestURI();

        // Filter 를 통과하지 않아도 되는 url
        if (isUrlInWhiteList(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization header 존재하는지 확인
        // 없으면 누구나 접근할 수 있는 페이지로 리다이렉트
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || authHeader.isEmpty()){
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Optional<AuthToken> token = tokenRepository.findAuthTokenByToken(authHeader.substring(7));

        if (token.isEmpty()){
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        httpRequest.setAttribute("AuthToken",token.get());
        filterChain.doFilter(request, response);
    }
}
