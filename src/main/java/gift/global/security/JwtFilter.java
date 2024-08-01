package gift.global.security;

import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter  {

    private final JwtUtil jwtUtil;

    private static final String BEAR_PREFIX = "Bearer ";
    private static final int BEAR_PREFIX_LENGTH = BEAR_PREFIX.length();
    public static final String REQUEST_ATTRIBUTE_NAME = "memberId";

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEAR_PREFIX)) {
            sendErrorCodeToResponse(response, ErrorCode.AUTHENTICATION_INVALID);
            return;
        }

        String token = authHeader.substring(BEAR_PREFIX_LENGTH);

        try {
            request.setAttribute(REQUEST_ATTRIBUTE_NAME, jwtUtil.extractId(token));
        } catch (CustomException exception) {
            sendErrorCodeToResponse(response, exception.getCode());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static void sendErrorCodeToResponse(
            HttpServletResponse response,
            ErrorCode code) throws IOException {
        response.sendError(
                code.getStatus()
                        .value(),
                code.getMessage()
        );
    }

}
