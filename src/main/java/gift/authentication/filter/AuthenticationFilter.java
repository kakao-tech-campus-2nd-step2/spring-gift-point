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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthenticationFilter extends OncePerRequestFilter {

    private final List<String> ignorePaths = new ArrayList<>();
    private final List<String> ignorePathsOnlyMethodGet = new ArrayList<>();
    private final String BEARER = "Bearer ";
    private final JwtResolver jwtResolver;

    public AuthenticationFilter(JwtResolver jwtResolver, List<String> ignorePaths, List<String> ignorePathsOnlyMethodGet) {
        this.jwtResolver = jwtResolver;
        this.ignorePaths.addAll(ignorePaths);
        this.ignorePathsOnlyMethodGet.addAll(ignorePathsOnlyMethodGet);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(Objects.nonNull(authorization) && authorization.startsWith(BEARER)) {
            String token = authorization.substring(BEARER.length());

            Long memberId = jwtResolver.resolveId(Token.from(token)).orElseThrow(() ->
                new InvalidCredentialsException(
                    "AuthenticationFilter.doFilterInternal.jwtResolver.resolveId 에서 예외 발생"));

            TokenContext.addCurrentMemberId(memberId);

            filterChain.doFilter(request, response);
            TokenContext.clear();
            return;
        }

        throw new JwtException("인증 토큰이 없습니다.");
    }

    /**
     * 필터링을 하지 않아야 하는 경로를 설정한다.<br><br>
     * 1. ignorePaths에 포함된 경로는 필터링을 하지 않는다.<br>
     * 2. HttpMethod이 OPTIONS인 경우 필터링을 하지 않는다.<br>
     * 3. ignorePathsOnlyMethodGet에 포함된 경로 중 HttpMethod.GET인 경우 필터링을 하지 않는다.
     * @param request 요청
     * @return 필터링을 하지 않아야 하는 경우 true, 그 외, false
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return ignorePaths.contains(requestURI) || isOptionsMethod(request) || isOptionalIgnorePathsWithGetMethod(request);
    }

    /**
     * ignorePathsOnlyMethodGet에 포함된 경로 중 HttpMethod.GET인 경우 필터링을 하지 않는다.
     * @param request 요청
     * @return ignorePathsOnlyMethodGet에 포함된 경로 중 HttpMethod.GET 경우 true 를 반환 그 외, false
     */
    private boolean isOptionalIgnorePathsWithGetMethod(HttpServletRequest request) {
        return ignorePathsOnlyMethodGet
            .stream()
            .anyMatch(regex -> Pattern.compile(regex).matcher(request.getRequestURI()).matches()
                && (isGetMethod(request)));
    }

    private boolean isGetMethod(HttpServletRequest request) {
        return request.getMethod().equals("GET");
    }

    private boolean isOptionsMethod(HttpServletRequest request) {
        return request.getMethod().equals("OPTIONS");
    }

}
