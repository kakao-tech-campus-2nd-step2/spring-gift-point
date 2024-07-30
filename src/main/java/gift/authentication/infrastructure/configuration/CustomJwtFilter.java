package gift.authentication.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.authentication.service.TokenProvider;
import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomJwtFilter extends OncePerRequestFilter {
    @Value("${app.headers.auth-token}")
    private String authTokenHeader;

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(CustomJwtFilter.class);


    @Autowired
    public CustomJwtFilter(TokenProvider tokenProvider, ObjectMapper objectMapper) {
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        if (shouldSkipRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader(authTokenHeader);
        if (token == null) {
            writeErrorResponse(response, ErrorCode.AUTHENTICATION_REQUIRED);
            return;
        }
        if (!token.startsWith("Bearer ")) {
            writeErrorResponse(response, ErrorCode.AUTHENTICATION_FAILED);
            return;
        }
        token = token.substring(7);
        try {
            Long userId = tokenProvider.extractUserId(token);
            request.setAttribute("userId", userId);
        } catch (APIException exception) {
            writeErrorResponse(response, ErrorCode.AUTHENTICATION_FAILED);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/members/login")
                || path.startsWith("/api/members/register")
                || path.startsWith("/api/oauth")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getStatus().value());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorCode.getMessage()));
        } catch (IOException exception) {
            logger.error("Failed to write error response", exception);
        }
    }
}
