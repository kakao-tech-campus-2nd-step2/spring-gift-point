package gift.utils.filter;

import gift.domain.Token;
import gift.repository.TokenRepository;
import gift.utils.ExternalApiService;
import gift.utils.JwtTokenProvider;
import gift.utils.error.TokenAuthException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class OAuthFilter implements Filter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final ExternalApiService externalApiService;

    public OAuthFilter(JwtTokenProvider jwtTokenProvider, TokenRepository tokenRepository,
        ExternalApiService externalApiService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRepository = tokenRepository;
        this.externalApiService = externalApiService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // CORS 헤더 설정
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://server.cla6sha.de");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");

        // OPTIONS 요청(프리플라이트) 처리
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String path = httpRequest.getRequestURI();

        // Filter 를 통과하지 않아도 되는 url
        if (path.equals("/api/user/login") || path.equals("/api/user/register") || path.startsWith("/user")
            || path.startsWith("/h2-console") || path.equals("/api/oauth/authorize")
            || path.equals("/api/oauth/token")
            || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")
            || path.startsWith("/swagger-resources") || path.equals("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/api/oauth")) {
            String authHeader = httpRequest.getHeader("Authorization");

            if (authHeader == null || authHeader.isEmpty()) {
                httpResponse.sendRedirect("http://server.cla6sha.de/login");
                return;
            }

            // JWT 토큰의 유효성 검사
            String token = authHeader.substring(7);

            String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
            Token tokenbyemail = tokenRepository.findByEmail(emailFromToken).orElseThrow(
                () -> new TokenAuthException("Oauth user not found")
            );
            LocalDateTime accessexpirationTime = tokenbyemail.getCreatetime().plusSeconds(tokenbyemail.getAccesstokentime());
            LocalDateTime refreshexpirationTime = tokenbyemail.getCreatetime().plusSeconds(tokenbyemail.getRefreshtokentime());

            if (accessexpirationTime.isBefore(LocalDateTime.now()) && refreshexpirationTime.isAfter(LocalDateTime.now())){
                externalApiService.refreshToken(emailFromToken,tokenbyemail.getRefreshtoken());
            }

            if (accessexpirationTime.isBefore(LocalDateTime.now()) && refreshexpirationTime.isBefore(LocalDateTime.now())){
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"No authentication token provided\", \"redirect\": \"http://server.cla6sha.de/login\"}");
                return;
            }

            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
