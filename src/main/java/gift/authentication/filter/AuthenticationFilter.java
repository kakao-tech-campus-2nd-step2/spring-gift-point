package gift.authentication.filter;

import gift.authentication.token.JwtResolver;
import gift.authentication.token.Token;
import gift.authentication.token.TokenContext;
import gift.web.validation.exception.client.InvalidCredentialsException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthenticationFilter extends OncePerRequestFilter {

    private final List<String> ignorePaths = List.of(
        "/api/members/login",
        "/api/members/register",
        "/api/login/oauth2/kakao");

    private final List<String> ignorePathsOnlyMethodGet = List.of(
        "/api/categories",
        "/api/products",
        "/api/products/\\d+",
        "/api/products/\\d+/options");
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER = "Bearer ";
    private final JwtResolver jwtResolver;

    public AuthenticationFilter(JwtResolver jwtResolver) {
        this.jwtResolver = jwtResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if(Objects.nonNull(authorization) && authorization.startsWith(BEARER)) {
            String token = authorization.substring(BEARER.length());

            Long memberId = jwtResolver.resolveId(Token.from(token)).orElseThrow(InvalidCredentialsException::new);
            TokenContext.addCurrentMemberId(memberId);

            filterChain.doFilter(request, response);
            TokenContext.clear();
            return;
        }

        throw new JwtException("Invalid token");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return ignorePaths.contains(requestURI) || isOptionalIgnorePathsWithGetMethod(request, requestURI);
    }

    /**
     * ignorePathsOnlyMethodGet에 포함된 경로 중 GET 메서드일 때만 필터링을 하지 않는다.
     * @param request
     * @param requestURI
     * @return ignorePathsOnlyMethodGet에 포함된 경로 중 GET 메서드인 경우 true 를 반환 그 외, false
     */
    private boolean isOptionalIgnorePathsWithGetMethod(HttpServletRequest request, String requestURI) {
        return ignorePathsOnlyMethodGet
            .stream()
            .anyMatch(regex -> Pattern.compile(regex).matcher(requestURI).matches()
                && request.getMethod().equals("GET"));
    }
}
