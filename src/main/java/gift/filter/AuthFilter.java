package gift.filter;

import gift.repository.token.TokenSpringDataJpaRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFilter implements Filter {

    private final TokenSpringDataJpaRepository tokenRepository;

    @Autowired
    public AuthFilter(TokenSpringDataJpaRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        if (isUnauthenticatedPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.sendRedirect("/home");
            return;
        }

        String token = authHeader.substring(7);

        if (!isTokenValid(token)) {
            httpResponse.sendRedirect("/home");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private boolean isUnauthenticatedPath(String path) {
        return path.equals("/home") || path.startsWith("/members")
                || path.startsWith("/h2-console") || path.startsWith("/api/products") || path.startsWith("/kakao/login")
                || path.startsWith("/api/categories") || path.startsWith("/swagger-ui") || path.startsWith("/api-docs");
    }

    private boolean isTokenValid(String token) {
        return tokenRepository.findByToken(token).isPresent();
    }
}
