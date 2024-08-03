package gift.utils.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.utils.JwtTokenProvider;
import gift.utils.error.ErrorResponse;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);


    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public AuthFilter(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
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
        if (path.equals("/api/members/login") || path.equals("/api/members/register") || path.startsWith("/api/members")
            || path.startsWith("/h2-console") || path.equals("/api/oauth/authorize") || path.startsWith("/api/categories")
            || path.startsWith("/api/products")
            || path.equals("/api/oauth/token")
            || path.equals("/swagger-ui.html")
            || path.startsWith("/swagger-ui")
            || path.startsWith("/api-docs")
            || path.startsWith("/v3/api-docs")
            || path.startsWith("/swagger-resources")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization header 존재하는지 확인
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || authHeader.isEmpty()) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "No authentication token provided");
            return;
        }

        // JWT 토큰의 유효성 검사
        String token = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            logger.info("Invalid token: {}", token);
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(statusCode, message);
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }


}
