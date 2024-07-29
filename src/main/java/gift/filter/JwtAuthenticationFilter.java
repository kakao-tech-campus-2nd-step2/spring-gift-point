package gift.filter;

import gift.util.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;

public class JwtAuthenticationFilter implements Filter {

    private final JwtUtil jwtUtil;

    private static final String HEADER_NAME = "Authorization";
    private static final String HEADER_VALUE = "Bearer ";

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authHeader = request.getHeader(HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(HEADER_VALUE)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
            return;
        }

        String token = authHeader.substring(7);
        String tokenUserId = jwtUtil.extractUserId(token);
        String place = "";
        place = containgTextReplace(request, "wishlist", place);
        place = containgTextReplace(request, "orders", place);

        String path = request.getRequestURI().split(request.getContextPath() + "/api/" + place + "/")[1];
        String[] pathParts = path.split("/");
        String userId = pathParts[0];

        if (!userId.equals(tokenUserId)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "유저 아이디가 토큰과 일치하지 않습니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private String containgTextReplace(HttpServletRequest request, String text, String place){
        if(request.getRequestURI().contains(text)){
            return text;
        }
        else return place;
    }
}
