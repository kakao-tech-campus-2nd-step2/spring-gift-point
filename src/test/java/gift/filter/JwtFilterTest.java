package gift.filter;

import gift.model.user.User;
import gift.util.UserUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.mockito.Mockito.*;

@SpringBootTest
public class JwtFilterTest {

    private String validToken;
    private JwtFilter jwtFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @Autowired
    private UserUtility userUtility;
    @Value("${spring.var.token-prefix}")
    private String tokenPrefix;

    @BeforeEach
    void setup() {
        jwtFilter = new JwtFilter(tokenPrefix, userUtility);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        validToken = userUtility.makeAccessToken(new User("test@naver.com", "test"));
    }

    @Test
    void doFilterWithoutTokenTest() throws ServletException, IOException {
        // given
        // when
        when(request.getHeader("Authorization")).thenReturn(null);
        jwtFilter.doFilter(request, response, filterChain);

        // then
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Test
    void doFilterWithValidTokenTest() throws ServletException, IOException {
        // given
        // when
        when(request.getHeader("Authorization")).thenReturn(tokenPrefix + validToken);
        jwtFilter.doFilter(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterWithInvalidTokenTest() throws ServletException, IOException {
        // given
        // when
        when(request.getHeader("Authorization")).thenReturn(tokenPrefix + validToken + "invalid");
        jwtFilter.doFilter(request, response, filterChain);

        // then
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
