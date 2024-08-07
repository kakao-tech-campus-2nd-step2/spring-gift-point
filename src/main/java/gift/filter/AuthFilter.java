package gift.filter;

import gift.domain.TokenAuth;
import gift.repository.token.TokenSpringDataJpaRepository;
import gift.service.TokenService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFilter implements Filter {

    private final TokenService tokenService;

    @Autowired
    public AuthFilter( TokenService tokenService) {
        this.tokenService = tokenService;
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

        String accessToken = authHeader.substring(7);

        if (tokenService.isAccessTokenValid(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        TokenAuth tokenAuth = tokenService.findTokenByAccessToken(accessToken);

        if (tokenAuth == null) {
            httpResponse.sendRedirect("/home");
            return;
        }

        String refreshToken = tokenAuth.getRefreshToken();

        if (refreshToken != null && tokenService.isRefreshTokenValid(refreshToken)) {
            String newAccessToken = tokenService.refreshAccessToken(refreshToken);
            httpResponse.setHeader("Authorization", "Bearer " + newAccessToken);
            filterChain.doFilter(request, response);
            return;
        }

        httpResponse.sendRedirect("/home");
    }

    @Override
    public void destroy() {
    }

    private boolean isUnauthenticatedPath(String path) {
        return path.equals("/home") || (path.startsWith("/api/members") && !path.equals("/api/members/point"))
                || path.startsWith("/h2-console") || path.startsWith("/api/products")
                || path.startsWith("/api/kakao") || path.startsWith("/api/categories")
                || path.startsWith("/swagger-ui") || path.startsWith("/api-docs")
                || path.startsWith("/api/orders/price");
    }

}
