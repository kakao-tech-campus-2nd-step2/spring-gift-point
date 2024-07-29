package gift.filter;

import gift.util.UserUtility;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminFilter implements Filter {

    private String tokenPrefix;
    private UserUtility userUtility;

    public AdminFilter(String tokenPrefix, UserUtility userUtility) {
        this.tokenPrefix = tokenPrefix;
        this.userUtility = userUtility;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        if (authorizationHeader.startsWith(tokenPrefix)) {
            String token = authorizationHeader.substring(tokenPrefix.length());
            Claims claims = userUtility.tokenParser(token);
            if (claims == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            String email = claims.get("email", String.class);
            String role = claims.get("role", String.class);

            if (email == null || role == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            if (role.equals("USER")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            servletRequest.setAttribute("email", email);
            servletRequest.setAttribute("role", role);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
